package com.yafnds.mapper;

import com.yafnds.base.BaseMapper;
import com.yafnds.entity.Admin;
import org.springframework.stereotype.Repository;

/**
 * 包名:com.yafnds.mapper
 *
 * @author Leevi
 * 日期2022-09-30  15:01
 */
@Repository
public interface AdminMapper extends BaseMapper<Admin> {

    Admin getByUsername(String username);
}
