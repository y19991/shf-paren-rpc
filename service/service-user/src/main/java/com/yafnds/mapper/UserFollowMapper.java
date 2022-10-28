package com.yafnds.mapper;

import com.github.pagehelper.Page;
import com.yafnds.entity.UserFollow;
import com.yafnds.entity.vo.UserFollowVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 类描述：
 * <p>创建时间：2022/10/17/017 16:58
 *
 * @author yafnds
 * @version 1.0
 */
@Repository
public interface UserFollowMapper {

    /**
     * 根据用户id和房源id查询关注信息
     * @param userId
     * @param houseId
     * @return
     */
    UserFollow findByUserIdAndHouseId(@Param("userId") Long userId, @Param("houseId") Long houseId);
    /**
     * 更新房源关注信息
     * @param userFollow
     */
    void update(UserFollow userFollow);

    /**
     * 新增关注信息
     * @param userFollow
     */
    void insert(UserFollow userFollow);

    /**
     * 分页查询用户的关注列表
     * @param userId
     * @return
     */
    Page<UserFollowVo> findListPage(@Param("userId") Long userId);
}