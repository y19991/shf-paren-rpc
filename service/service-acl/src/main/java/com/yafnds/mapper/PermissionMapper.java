package com.yafnds.mapper;

import com.yafnds.base.BaseMapper;
import com.yafnds.entity.Permission;

import java.util.List;
import java.util.Map;

/**
 * 类描述：
 * <p>创建时间：2022/10/18/018 16:04
 *
 * @author yafnds
 * @version 1.0
 */

public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 查询所有权限
     * @return
     */
    List<Permission> findAll();

    /**
     * 查询用户的菜单权限列表
     * @param adminId
     * @return
     */
    List<Permission> findPermissionListByAdminId(Long adminId);

    /**
     * 根据父id查找权限集合
     * @param parentId
     * @return
     */
    List<Permission> findByParentId(Long parentId);
}
