package com.yafnds.service;

import com.yafnds.base.BaseService;
import com.yafnds.entity.Role;

import java.util.List;
import java.util.Map;

/**
 * 类描述：
 * <p>创建时间：2022/10/2/002 15:45
 *
 * @author yafnds
 * @version 1.0
 */
public interface RoleService extends BaseService<Role> {

    /**
     * 查询用户的角色列表
     * @param adminId
     * @return
     */
    Map<String,List<Role>> findRolesByAdminId(Long adminId);

    /**
     * 保存用户角色
     * @param adminId
     * @param roleIdList
     */
    void saveAdminRole(Long adminId,List<Long> roleIdList);

    /**
     * 根据用户id查询角色列表
     * @param adminId
     * @return
     */
    List<Role> findByAdminId(Long adminId);
}
