package com.yafnds.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.yafnds.base.BaseMapper;
import com.yafnds.base.BaseServiceImpl;
import com.yafnds.entity.UserInfo;
import com.yafnds.mapper.UserInfoMapper;
import com.yafnds.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 类描述：用户信息 持久层接口
 * <p>创建时间：2022/10/14/014 16:39
 *
 * @author yafnds
 * @version 1.0
 */
@Service(interfaceClass = UserInfoService.class)
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    protected BaseMapper<UserInfo> getEntityMapper() {
        return userInfoMapper;
    }

    /**
     * 根据电话号码查询用户信息
     * @param phone 电话号码
     * @return
     */
    @Override
    public UserInfo getByPhone(String phone) {
        return userInfoMapper.getByPhone(phone);
    }
}
