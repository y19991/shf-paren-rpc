package com.yafnds.mapper;

import com.yafnds.base.BaseMapper;
import com.yafnds.entity.UserInfo;
import org.springframework.stereotype.Repository;

/**
 * 类描述： 用户信息 持久层接口
 * <p>创建时间：2022/10/14/014 15:05
 *
 * @author yafnds
 * @version 1.0
 */
@Repository
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    UserInfo getByPhone(String phone);

}
