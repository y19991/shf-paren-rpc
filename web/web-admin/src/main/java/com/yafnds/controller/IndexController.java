package com.yafnds.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yafnds.entity.Admin;
import com.yafnds.entity.Permission;
import com.yafnds.entity.Role;
import com.yafnds.service.AdminService;
import com.yafnds.service.PermissionService;
import com.yafnds.service.RoleService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 类描述：主页控制类
 * <p>创建时间：2022/10/18/018 10:56
 *
 * @author yafnds
 * @version 1.0
 */
@Controller
public class IndexController {

    @Reference
    private AdminService adminService;
    @Reference
    private PermissionService permissionService;
    @Reference
    private RoleService roleService;

    private static final String PAGE_INDEX = "frame/index";

    /**
     * 主页
     *
     * @param model
     * @return
     */
    @GetMapping("/")
    public String index(Model model) {

        //由SpringSecurity获取当前登录的用户
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //从user中获取账号username，然后根据username可以查询到admin
        Admin admin = adminService.getByUsername(user.getUsername());

        //1. 查询出当前登录的用户信息存储到请求域
        model.addAttribute("admin",admin);

        //2. 查询当前登录的用户的角色列表存储到请求域
        List<Role> roleList = roleService.findByAdminId(admin.getId());
        model.addAttribute("roleList",roleList);

        //3. 查询出当前登录的用户的动态菜单存储到请求域
        List<Permission> menu = permissionService.findMenuPermissionByAdminId(admin.getId());
        model.addAttribute("permissionList",menu);
        return PAGE_INDEX;
    }
}
