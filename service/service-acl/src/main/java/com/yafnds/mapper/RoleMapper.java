package com.yafnds.mapper;

import com.yafnds.base.BaseMapper;
import com.yafnds.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 类描述：角色表 dao层
 * <p>创建时间：2022/9/29/029 20:42
 *
 * @author yafnds
 * @version 1.0
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 查询全部角色
     */
    List<Role> findAll();

    List<Role> findByAdminId(Long adminId);
}
