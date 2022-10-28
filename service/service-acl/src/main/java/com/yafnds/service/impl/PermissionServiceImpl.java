package com.yafnds.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.yafnds.base.BaseMapper;
import com.yafnds.base.BaseServiceImpl;
import com.yafnds.entity.Permission;
import com.yafnds.entity.RolePermission;
import com.yafnds.helper.PermissionHelper;
import com.yafnds.mapper.PermissionMapper;
import com.yafnds.mapper.RolePermissionMapper;
import com.yafnds.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：
 * <p>创建时间：2022/10/18/018 16:01
 *
 * @author yafnds
 * @version 1.0
 */
@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl  extends BaseServiceImpl<Permission> implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    protected BaseMapper<Permission> getEntityMapper() {
        return permissionMapper;
    }

    /**
     * 查询用户的所有权限
     * @param adminId
     * @return
     */
    @Override
    public List<Permission> findMenuPermissionByAdminId(Long adminId) {

        List<Permission> permissionList = null;
        // 判断是否是超级管理员
        if (adminId == 1) {
            //超级管理员
            permissionList = permissionMapper.findAll();
        }else {
            // 根据adminId查询权限列表
            permissionList = permissionMapper.findPermissionListByAdminId(adminId);
        }

        // 构建树形菜单
        return PermissionHelper.build(permissionList);
    }

    /**
     * 根据角色获取权限数据
     * @param roleId
     * @return
     */
    @Override
    public List<Map<String, Object>> findPermissionByRoleId(Long roleId) {

        // 1. 查出所有权限
        List<Permission> allPermissionList = permissionMapper.findAll();
        // 2. 查询当前已分配的权限id
        List<Long> assignPermissionIdList = rolePermissionMapper.findPermissionIdListByRoleId(roleId);

        // 3. 将每一个 Permission 映射成一个对应的 Map，其中包含五个需要的键值对
        List<Map<String, Object>> zNodes = allPermissionList.stream()
                .map(permission -> {
                    Map<String, Object> zNode = new HashMap<>();
                    // 设置键值对
                    zNode.put("id", permission.getId());
                    zNode.put("pId", permission.getParentId());
                    zNode.put("name", permission.getName());
                    // 判断当前permission是否存在子节点，有则设置open = true
                    if (!CollectionUtils.isEmpty(permissionMapper.findByParentId(permission.getId()))) {
                        zNode.put("open", true);
                    }
                    // 判断当前角色是否被分配了权限，如果有，则设置为true
                    zNode.put("checked", assignPermissionIdList.contains(permission.getId()));

                    return zNode;
                }).collect(Collectors.toList());

        return zNodes;
    }

    /**
     * 给角色分配权限
     * @param roleId
     * @param permissionIds
     */
    @Override
    public void saveRolePermission(Long roleId, List<Long> permissionIds) {

        // 1. 找出需要删除的，进行删除
        // 1.1 找出已分配给该角色的权限
        List<Long> assignPermissionIdList = rolePermissionMapper.findPermissionIdListByRoleId(roleId);
        // 1.2 创建一个集合用来存储需要删除的权限id
        List<Long> removePermissionIdList = new ArrayList<>();
        // 1.3 判断 permissionIds 是否为空（判断这次是否需要分配权限）
        if (!CollectionUtils.isEmpty(permissionIds)) {
            // 1.3.1 判断在之前有没有分配过权限
            if (!CollectionUtils.isEmpty(assignPermissionIdList)) {
                // 1.3.2 如果之期就分配过权限，那么这次不需要分配的
                for (Long assignPermissionId : assignPermissionIdList) {
                    if (!permissionIds.contains(assignPermissionId)) {
                        removePermissionIdList.add(assignPermissionId);
                    }
                }
            }
        } else {
            // 1.3.3 如果这次不需要分配任何权限，则将之前分配的全部删除
            removePermissionIdList = assignPermissionIdList;
        }
        // 1.4 执行删除
        if (!CollectionUtils.isEmpty(removePermissionIdList)) {
            rolePermissionMapper.deletByRoleIdAndPermissionIdList(roleId, removePermissionIdList);
        }

        // 2. 重新分配
        // 2.1 判断 目标权限列表 是否为空，如果不为空，则重新分配
        if (!CollectionUtils.isEmpty(permissionIds)) {
            for (Long permissionId : permissionIds) {

                // 拿目标用户和目标权限列表进行查询
                RolePermission rolePermission = rolePermissionMapper.findByRoleIdAndPermissionId(roleId, permissionId);

                // 2.2 判断是否分配过权限
                if (!ObjectUtils.isEmpty(rolePermission)) {
                    if (1 == rolePermission.getIsDeleted()) {
                        // 2.2.1 曾经分配过，但是删除了，将其加回来
                        rolePermission.setIsDeleted(0);
                        rolePermissionMapper.update(rolePermission);
                    }
                } else {
                    // 2.2.2 从未分配过，直接新增
                    rolePermission = new RolePermission();
                    rolePermission.setRoleId(roleId);
                    rolePermission.setPermissionId(permissionId);
                    rolePermissionMapper.insert(rolePermission);
                }
            }
        }
    }

    /**
     * 根据用户id查询用户的三级菜单的code列表
     * @param adminId
     * @return
     */
    @Override
    public List<String> findPermissionCodeListByAdminId(Long adminId) {

        List<Permission> permissionList = null;
        // 1. 根据用户id查询用户的所有权限(如果是超级管理员，那么应该查询所有权限)
        if (adminId == 1) {
            permissionList = permissionMapper.findAll();
        }else {
            permissionList = permissionMapper.findPermissionListByAdminId(adminId);
        }
        // 2. 过滤出三级菜单、并且将permission对象映射成String
        List<String> permissionCodeList = null;
        if (!CollectionUtils.isEmpty(permissionList)) {

            permissionCodeList = permissionList.stream()
                    .filter(permission -> permission.getType() == 2)
                    .map(Permission::getCode)
                    .collect(Collectors.toList());
        }
        return permissionCodeList;
    }
}
