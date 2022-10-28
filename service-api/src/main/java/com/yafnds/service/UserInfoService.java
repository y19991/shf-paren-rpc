package com.yafnds.service;

import com.yafnds.base.BaseService;
import com.yafnds.entity.UserInfo;

/**
 * 类描述：用户信息（前台页面） 业务层
 * <p>创建时间：2022/10/14/014 16:34
 *
 * @author yafnds
 * @version 1.0
 */

public interface UserInfoService extends BaseService<UserInfo> {

    /**
     * 根据电话号码查询用户信息
     * @param phone 电话号码
     * @return 用户信息
     */
    UserInfo getByPhone(String phone);
}
