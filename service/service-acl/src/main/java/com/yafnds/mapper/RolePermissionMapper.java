package com.yafnds.mapper;

import com.yafnds.base.BaseMapper;
import com.yafnds.entity.RolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：
 * <p>创建时间：2022/10/18/018 16:06
 *
 * @author yafnds
 * @version 1.0
 */

public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    /**
     * 根据roleId查询permissionId集合
     * @param roleId
     * @return
     */
    List<Long> findPermissionIdListByRoleId(Long roleId);

    /**
     * 根据需要删除的列表和角色id删除对应权限
     * @param roleId
     * @param removePermissionIdList
     */
    void deletByRoleIdAndPermissionIdList(@Param("roleId") Long roleId,
                                          @Param("removePermissionIdList") List<Long> removePermissionIdList);

    RolePermission findByRoleIdAndPermissionId(@Param("roleId") Long roleId,@Param("permissionId") Long permissionId);
}
