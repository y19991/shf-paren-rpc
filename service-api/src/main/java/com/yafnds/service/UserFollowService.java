package com.yafnds.service;

import com.github.pagehelper.PageInfo;
import com.yafnds.entity.UserFollow;
import com.yafnds.entity.vo.UserFollowVo;

/**
 * 类描述：
 * <p>创建时间：2022/10/17/017 16:55
 *
 * @author yafnds
 * @version 1.0
 */
public interface UserFollowService {
    /**
     * 根据用户id和房源id查询关注信息
     * @param userId
     * @param houseId
     * @return
     */
    UserFollow findByUserIdAndHouseId(Long userId, Long houseId);

    /**
     * 更新房源的关注信息
     * @param userFollow
     */
    void update(UserFollow userFollow);

    /**
     * 新增关注信息
     * @param userFollow
     */
    void insert(UserFollow userFollow);

    /**
     * 分页查询用户关注列表
     * @param pageNum
     * @param pageSize
     * @param userId
     * @return
     */
    PageInfo<UserFollowVo> findListPage(int pageNum, int pageSize, Long userId);
}
