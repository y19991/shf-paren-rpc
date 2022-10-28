package com.yafnds.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yafnds.entity.Admin;
import com.yafnds.service.AdminService;
import com.yafnds.service.PermissionService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：
 * <p>创建时间：2022/10/19/019 19:14
 *
 * @author yafnds
 * @version 1.0
 */
@Component
public class UserDetailServiceImpl implements UserDetailsService {

    @Reference
    private AdminService adminService;
    @Reference
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. 根据用户名查找用户
        Admin admin = adminService.getByUsername(username);
        // 1.1 如果adnin为null，说明用户名错误
        if (ObjectUtils.isEmpty(admin)) {
            throw new UsernameNotFoundException("用户名错误");
        }
        // 1.2 用户名正确，开始效验密码(将数据库中该用户的密码交给SpringSecurity)
        // 1.3 授权: 查询到当前用户有哪些权限,然后将其告诉SpringSecurity
        // 1.3.1 查询出当前用户有哪些权限(只需要查询三级菜单):
        // List<String> ------> 怎么用一个字符串表示一个权限呢? ------> code字段的值
        List<String> permissionCodeList = permissionService.findPermissionCodeListByAdminId(admin.getId());

        // 1.3.2 将当前用户拥有的权限告诉SpringSecurity: List<SimpleGrantedAuthority>
        List<SimpleGrantedAuthority> grantedAuthorityList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(permissionCodeList)) {
            grantedAuthorityList = permissionCodeList.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        return new User(username, admin.getPassword(), grantedAuthorityList);
    }
}
