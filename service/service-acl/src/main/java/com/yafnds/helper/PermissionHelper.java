package com.yafnds.helper;

import com.yafnds.entity.Permission;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：辅助构建树形菜单
 * <p>创建时间：2022/10/18/018 10:59
 *
 * @author yafnds
 * @version 1.0
 */

public class PermissionHelper {

    /**
     * 设置菜单的父子结构
     * @param permissionList
     * @return
     */
    public static List<Permission> build(List<Permission> permissionList) {

        List<Permission> menu = new ArrayList<>();
        if (!CollectionUtils.isEmpty(permissionList)) {
            // 1. 从当前用户的所有权限冲筛选出一级菜单
            menu = permissionList.stream()
                    .filter(permission -> permission.getParentId() == 0)
                    .collect(Collectors.toList());

            // 2. 对每一个一级菜单构建子菜单
            for (Permission permission : menu) {
                permission.setLevel(1);
                setChildren(permissionList, permission);
            }
        }
        return menu;
    }

    /**
     * 从permissionList中找出permission的所有子菜单，设置给permission
     * @param permissionList
     * @param permission
     */
    private static void setChildren(List<Permission> permissionList, Permission permission) {

        // 创建一个集合用来存储permission的子菜单
        List<Permission> children = new ArrayList<>();
        for (Permission child : permissionList) {
            if (child.getParentId().equals(permission.getId())) {
                // 满足条件，将其设置为子菜单
                child.setLevel(permission.getLevel() + 1); // 设置子菜单的层级为：父菜单的层级 + 1
                child.setParentName(permission.getName());
                children.add(child);

                // 设置子菜单的子菜单
                setChildren(permissionList, child);
            }
        }
        permission.setChildren(children);
    }
}
