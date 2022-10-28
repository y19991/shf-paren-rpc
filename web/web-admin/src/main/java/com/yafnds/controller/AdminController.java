package com.yafnds.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.yafnds.base.BaseController;
import com.yafnds.entity.Admin;
import com.yafnds.entity.Role;
import com.yafnds.service.AdminService;
import com.yafnds.service.RoleService;
import com.yafnds.util.FileUtil;
import com.yafnds.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 类描述：用户管理
 * <p>创建时间：2022/09/30 15:49
 *
 * @author yafnds
 * @version 1.0
 */
@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {

    @Reference
    private AdminService adminService;
    @Reference
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String PAGE_INDEX = "admin/index";
    private static final String LIST_ACTION = "redirect:/admin";
    private static final String PAGE_EDIT = "admin/edit";
    private static final String PAGE_UPLOAD_SHOW = "admin/upload";
    private static final String PAGE_ASSIGN_SHOW = "admin/assignShow";


    @RequestMapping
    public String index(@RequestParam Map<String,Object> filters, Model model){
        //1. 调用业务层的方法查询分页数据
        PageInfo<Admin> pageInfo = adminService.findPage(filters);
        //2. 将分页数据存储到请求域
        model.addAttribute("page",pageInfo);
        //3. 将搜索条件存储到请求域
        model.addAttribute("filters",filters);
        return PAGE_INDEX;
    }

    @PostMapping("/save")
    public String save(Admin admin,Model model){

        //0. 使用SpringSecurity所使用的加密器对admin中的密码进行加密
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        //1. 调用业务层的方法保存用户信息
        adminService.insert(admin);
        //2. 显示成功页面
        return successPage("保存用户信息成功", model);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){

        // 1. 调用业务层的方法根据id删除
        adminService.delete(id);

        // 2. 删除七牛云中的原图片
        // 2.1 获得七牛云中的图片名
        String oldHeadUrl = adminService.findById(id).getHeadUrl();
        if (!StringUtils.isEmpty(oldHeadUrl)) {
            String oldFileName = QiniuUtils.getFileNameFromUrl(oldHeadUrl);
            // 2.1 从七牛云中删除原来的图片
            QiniuUtils.deleteFileFromQiniu(oldFileName);
        }

        //2. 重定向访问首页
        return LIST_ACTION;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id,Model model){
        //1. 调用业务层的方法根据id查询
        Admin admin = adminService.findById(id);
        //2. 将查询到的用户信息存储到请求域
        model.addAttribute("admin",admin);
        return PAGE_EDIT;
    }

    @PostMapping("/update")
    public String update(Admin admin,Model model){
        //1. 调用业务层的方法修改用户
        adminService.update(admin);
        //2. 显示成功页面
        return successPage("修改用户信息成功", model);
    }

    /**
     * 跳转至头像上传页面
     * @param id 用户 id
     */
    @GetMapping("/uploadShow/{id}")
    public String uploadShow(@PathVariable("id") Long id,Model model){
        model.addAttribute("id",id);
        return PAGE_UPLOAD_SHOW;
    }

    /**
     * 用户头像上传
     * @param id    用户 id
     * @param file  用户头像
     * @param model
     * @return 跳转到成功页面
     */
    @PostMapping("/upload/{id}")
    public String upload(@PathVariable Long id,
                         @RequestParam("file") MultipartFile file,
                         Model model) throws IOException {

        // 1。将图片上传至七牛云
        // 1.1 生成唯一的文件名
        String uuidName = FileUtil.getUUIDName(file.getOriginalFilename());
        // 1.2 规范文件存储（指定存储目录，按日期分类）
        String dateDirPath = FileUtil.getDateDirPath();
        String fileName = dateDirPath + uuidName;
        // 1.3 上传文件至七牛云
        QiniuUtils.upload2Qiniu(file.getBytes(), fileName);

        // 2. 删除七牛云中的原图片
        // 2.1 获得七牛云中的图片名
        String oldHeadUrl = adminService.findById(id).getHeadUrl();
        if (!StringUtils.isEmpty(oldHeadUrl)) {
            String oldFileName = QiniuUtils.getFileNameFromUrl(oldHeadUrl);
            // 2.1 从七牛云中删除原来的图片
            QiniuUtils.deleteFileFromQiniu(oldFileName);
        }

        // 3. 将图片信息保存到数据库
        // 3.1 获取图片链接
        String headUrl = QiniuUtils.getUrl(fileName);
        // 3.2 修改数据库
        Admin admin = new Admin();
        admin.setId(id);
        admin.setHeadUrl(headUrl);
        adminService.update(admin);

        return successPage("上传头像成功", model);
    }

    /**
     * 给用户分配角色
     * @param id 角色id
     * @param model
     * @return
     */
    @GetMapping("/assignShow/{id}")
    public String assignShow(@PathVariable Long id, Model model){
        //1. 调用业务层的方法获取用户已分配和未分配的角色列表
        Map<String, List<Role>> roleListMap = roleService.findRolesByAdminId(id);
        //2. 将查询到的数据存储到请求域,请求域中的key分别是assignRoleList和unAssignRoleList

        //3. 将id存储到请求域: 为了在分配角色页面中拿到adminId
        model.addAttribute("adminId",id);

        model.addAllAttributes(roleListMap);
        return PAGE_ASSIGN_SHOW;
    }

    /**
     * 保存角色
     * @param adminId
     * @param roleIds
     * @param model
     * @return
     */
    @PostMapping("/assignRole")
    public String assignRole(@RequestParam("adminId") Long adminId,
                             @RequestParam("roleIds") List<Long> roleIds,
                             Model model){

        roleService.saveAdminRole(adminId,roleIds);

        return successPage("保存角色成功", model);
    }
}
