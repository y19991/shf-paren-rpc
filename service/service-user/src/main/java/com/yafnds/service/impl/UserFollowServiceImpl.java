package com.yafnds.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yafnds.entity.UserFollow;
import com.yafnds.entity.vo.UserFollowVo;
import com.yafnds.mapper.UserFollowMapper;
import com.yafnds.service.UserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 类描述：
 * <p>创建时间：2022/10/17/017 16:57
 *
 * @author yafnds
 * @version 1.0
 */

@Transactional
@Service(interfaceClass = UserFollowService.class)
public class UserFollowServiceImpl implements UserFollowService {

    @Autowired
    private UserFollowMapper userFollowMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public UserFollow findByUserIdAndHouseId(Long userId, Long houseId) {

        return userFollowMapper.findByUserIdAndHouseId(userId,houseId);
    }

    @Override
    public void update(UserFollow userFollow) {
        userFollowMapper.update(userFollow);
    }

    @Override
    public void insert(UserFollow userFollow) {
        userFollowMapper.insert(userFollow);
    }

    @Override
    public PageInfo<UserFollowVo> findListPage(int pageNum, int pageSize, Long userId) {
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<>(userFollowMapper.findListPage(userId),10);
    }
}