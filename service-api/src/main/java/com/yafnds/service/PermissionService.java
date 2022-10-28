package com.yafnds.service;

import com.yafnds.entity.Permission;

import java.util.List;
import java.util.Map;

/**
 * 类描述：
 * <p>创建时间：2022/10/18/018 15:59
 *
 * @author yafnds
 * @version 1.0
 */

public interface PermissionService {

    /**
     * 查询用户的所有权限
     * @param adminId
     * @return
     */
    List<Permission> findMenuPermissionByAdminId(Long adminId);

    /**
     * 根据角色获取权限数据
     * @param roleId
     * @return
     */
    List<Map<String, Object>> findPermissionByRoleId(Long roleId);

    /**
     * 给角色分配权限
     * @param roleId
     * @param permissionIds
     */
    void saveRolePermission(Long roleId, List<Long> permissionIds);

    /**
     * 根据用户id查询用户的三级菜单的code列表
     * @param adminId
     * @return
     */
    List<String> findPermissionCodeListByAdminId(Long adminId);
}
