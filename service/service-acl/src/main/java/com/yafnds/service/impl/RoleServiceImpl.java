package com.yafnds.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.yafnds.base.BaseMapper;
import com.yafnds.base.BaseServiceImpl;
import com.yafnds.entity.AdminRole;
import com.yafnds.entity.Role;
import com.yafnds.mapper.AdminRoleMapper;
import com.yafnds.mapper.RoleMapper;
import com.yafnds.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：
 * <p>创建时间：2022/10/2/002 15:48
 *
 * @author yafnds
 * @version 1.0
 */

@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;

    /**
     * 获取 roleMapper
     */
    @Override
    protected BaseMapper<Role> getEntityMapper() {
        return roleMapper;
    }

    /**
     * 查询用户列表
     * @param adminId
     * @return
     */
    @Override
    public Map<String, List<Role>> findRolesByAdminId(Long adminId) {
        //1. 查询所有角色
        List<Role> allRoleList = roleMapper.findAll();
        //2. 查询用户已分配的角色id列表
        List<Long> assignRoleIdList = adminRoleMapper.findRoleIdListByAdminId(adminId);
        //3. 创建俩List分别存储用户已分配和未分配的角色
        List<Role> unAssignRoleList = new ArrayList<>();
        List<Role> assignRoleList = new ArrayList<>();
        //4. 筛选用户已分配和未分配角色
        for (Role role : allRoleList) {
            if (assignRoleIdList.contains(role.getId())) {
                //已分配
                assignRoleList.add(role);
            }else {
                //未分配
                unAssignRoleList.add(role);
            }
        }
        Map<String,List<Role>> roleMap = new HashMap<>();
        roleMap.put("unAssignRoleList",unAssignRoleList);
        roleMap.put("assignRoleList",assignRoleList);
        return roleMap;
    }

    /**
     * 保存用户角色（复杂做法）
     * @param adminId    需要分配角色的用户 id
     * @param roleIdList 目标分配角色 id 集合
     */
    @Override
    public void saveAdminRole(Long adminId, List<Long> roleIdList) {

        // 1. 找出需要删除的那些分配信息的角色id集合: 之前已分配、但是这次不要分配了
        // 1.1 找出该用户之前已分配的角色的id集合
        List<Long> assignRoleIdList = adminRoleMapper.findRoleIdListByAdminId(adminId);
        // 声明一个集合用来存储要删除的那些分配信息的角色id集合
        List<Long> removeRoleIdList = new ArrayList<>();

        // 1.2 判断 已分配角色的id集合 是否空
        if (!CollectionUtils.isEmpty(assignRoleIdList)) {
            // 1.3 如果为空，则获取 需要删除的角色id集合
            // 1.3.1 判断 目标分配角色id集合 是否为空
            if (CollectionUtils.isEmpty(roleIdList)) {
                // 如果为空，表示将之前已分配的角色id全部删除
                removeRoleIdList = assignRoleIdList;
            } else {
                // 如果不为空，则将在 已分配角色id列表 但是不在 目标分配角色id列表 中的角色id加入删除列表
                for (Long roleId : assignRoleIdList) {
                    if (!roleIdList.contains(roleId)) {
                        removeRoleIdList.add(roleId);
                    }
                }
            }
        }
        // 1.3 在库中移除该删除的角色id
        if (!CollectionUtils.isEmpty(removeRoleIdList)) {
            adminRoleMapper.deleteByAdminIdAndRoleIdList(adminId,removeRoleIdList);
        }

        // 2. 找出这次需要新增的
        // 2.1 根据当前用户id，查出当前用户已分配的所有信息（包含被删除的）
        if (!CollectionUtils.isEmpty(roleIdList)) {
            for (Long roleId : roleIdList) {
                // 在目标列表中，但不在已分配中，即为本次需要新增的信息
                AdminRole adminRole = adminRoleMapper.findByAdminIdAndRoleId(adminId, roleId);
                if (null != adminRole) {
                    // 说明曾经分配过，需要查询是否被删除了
                    if (1 == adminRole.getIsDeleted()) {
                        adminRole.setIsDeleted(0);
                        adminRoleMapper.update(adminRole);
                    }
                } else {
                    adminRole = new AdminRole();
                    adminRole.setRoleId(roleId);
                    adminRole.setAdminId(adminId);
                    adminRoleMapper.insert(adminRole);
                }
            }
        }

    }

    /**
     * 根据用户id查询角色列表
     * @param adminId
     * @return
     */
    @Override
    public List<Role> findByAdminId(Long adminId) {
        return roleMapper.findByAdminId(adminId);
    }
}
