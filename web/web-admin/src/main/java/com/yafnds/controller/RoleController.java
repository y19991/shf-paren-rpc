package com.yafnds.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.yafnds.base.BaseController;
import com.yafnds.entity.Role;
import com.yafnds.result.Result;
import com.yafnds.service.PermissionService;
import com.yafnds.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 类描述：角色管理
 * <p>创建时间：2022/10/3/003 16:59
 *
 * @author yafnds
 * @version 1.0
 */

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Reference
    private RoleService roleService;
    @Reference
    private PermissionService permissionService;

    private static final String LIST_ACTION = "redirect:/role";
    private static final String PAGE_INDEX = "role/index";
    private static final String PAGE_EDIT = "role/edit";
    private static final String PAGE_ASSIGN_SHOW = "role/assignShow";


    /**
     * 分页显示角色列表
     * @param filters 查询条件
     * @return 跳转至角色列表（角色管理主页）
     */
    @RequestMapping
    public String index(@RequestParam Map<String, Object> filters, Model model) {

        PageInfo<Role> pageInfo = roleService.findPage(filters);
        model.addAttribute("page", pageInfo);
        model.addAttribute("filters", filters);

        return PAGE_INDEX;
    }

    /**
     * 新增一个角色 POST
     * @param role 要新增的角色
     * @return 跳转至角色新增成功页面
     */
    @PostMapping("/save")
    public String save(Role role, Model model) {
        roleService.insert(role);
        return successPage("添加角色成功", model);
    }

    /**
     * 跳转至修改页面 GET
     * @param id 需要修改的角色 id，用于查询对应角色数据
     * @return 跳转至修改页面
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Role role = roleService.findById(id);
        model.addAttribute("role", role);
        return PAGE_EDIT;
    }

    /**
     * 修改角色数据 POST
     * @param role 需要修改的角色
     * @return 跳转至成功页面
     */
    @PostMapping("/update")
    public String update(Role role, Model model) {
        roleService.update(role);
        return successPage("更新角色成功", model);
    }

    /**
     * 删除角色
     * @param id 要删除的角色 id
     * @return 跳转至角色列表
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        roleService.delete(id);
        return LIST_ACTION;
    }

    /**
     * 显示分配权限页面
     * @param roleId
     * @param model
     * @return
     */
    @GetMapping("/assignShow/{roleId}")
    public String assignShow(@PathVariable Long roleId,Model model){
        //1. 将roleId存储到请求域
        model.addAttribute("roleId",roleId);
        //2. 调用业务层的方法查询所有权限(并且要满足客户端的需要的形状)
        List<Map<String,Object>> zNodes = permissionService.findPermissionByRoleId(roleId);
        //3. 将zNodes转成JSON字符串存储到请求域
        model.addAttribute("zNodes", JSON.toJSONString(zNodes));

        return PAGE_ASSIGN_SHOW;
    }

    /**
     * 保存分配权限
     * @param roleId 要分配权限的角色的id
     * @param permissionIds 要分配给该角色的所有权限的id
     * @param model
     * @return
     */
    @PostMapping("/assignPermission")
    public String assignPermission(@RequestParam Long roleId, @RequestParam List<Long> permissionIds, Model model){

        permissionService.saveRolePermission(roleId,permissionIds);

        return successPage("设置角色权限成功", model);
    }

}
