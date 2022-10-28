package com.yafnds.mapper;

import com.yafnds.base.BaseMapper;
import com.yafnds.entity.AdminRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 类描述：
 * <p>创建时间：2022/10/17/017 20:51
 *
 * @author yafnds
 * @version 1.0
 */
@Repository
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    /**
     * 根据用户id查询其分配的角色id列表
     * @param adminId
     * @return
     */
    List<Long> findRoleIdListByAdminId(Long adminId);

    /**
     * 查询用户是否绑定过该角色
     * @param adminId
     * @param roleId
     * @return
     */
    AdminRole findByAdminIdAndRoleId(@Param("adminId") Long adminId, @Param("roleId") Long roleId);

    /**
     * 移除用户的角色
     * @param adminId
     * @param removeRoleIdList
     */
    void removeAdminRole(@Param("adminId") Long adminId, @Param("roleIds") List<Long> removeRoleIdList);

    /**
     * 查询用户是否绑定过该角色
     * @param adminId
     * @param removeRoleIdList
     */
    void deleteByAdminIdAndRoleIdList(@Param("adminId") Long adminId, @Param("removeRoleIdList") List<Long> removeRoleIdList);
}
