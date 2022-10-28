package com.yafnds.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.yafnds.base.BaseMapper;
import com.yafnds.base.BaseServiceImpl;
import com.yafnds.entity.Admin;
import com.yafnds.mapper.AdminMapper;
import com.yafnds.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 包名:com.yafnds.service.impl
 *
 * @author Leevi
 * 日期2022-09-30  15:09
 */
@Service(interfaceClass = AdminService.class)
public class AdminServiceImpl extends BaseServiceImpl<Admin> implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public BaseMapper<Admin> getEntityMapper() {
        return adminMapper;
    }

    @Override
    public Admin getByUsername(String username) {
        return adminMapper.getByUsername(username);
    }
}
