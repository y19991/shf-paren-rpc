package com.yafnds.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.yafnds.entity.UserFollow;
import com.yafnds.entity.UserInfo;
import com.yafnds.entity.vo.UserFollowVo;
import com.yafnds.result.Result;
import com.yafnds.service.UserFollowService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 类描述：
 * <p>创建时间：2022/10/17/017 16:54
 *
 * @author yafnds
 * @version 1.0
 */

@RestController
@RequestMapping("/userFollow")
public class UserFollowController {

    @Reference
    private UserFollowService userFollowService;

    /**
     * 添加关注
     * @param houseId
     * @param session
     * @return
     */
    @GetMapping("/auth/follow/{houseId}")
    public Result addFollow(@PathVariable("houseId") Long houseId, HttpSession session){
        //1. 判断用户之前是否已经添加过这条数据的关注:根据用户id和房源id查询出UserFollow
        //1.1 获取当前登录的用户
        UserInfo userInfo = (UserInfo) session.getAttribute("USER");
        UserFollow userFollow = userFollowService.findByUserIdAndHouseId(userInfo.getId(),houseId);
        //2.如果用户之前已经添加过关注，那么我们只需要更新这条数据的is_deleted为0

        if (userFollow != null) {
            //说明用户之前关注过
            userFollow.setIsDeleted(0);
            //更新关注
            userFollowService.update(userFollow);
        }else {
            //3. 如果用户之前没有添加过关注，我们需要新增一条数据
            userFollow = new UserFollow();
            userFollow.setUserId(userInfo.getId());
            userFollow.setHouseId(houseId);
            userFollowService.insert(userFollow);
        }

        return Result.ok();
    }

    /**
     * 分页查询用户关注列表
     * @param pageNum 当前页数
     * @param pageSize 当前页数据条数
     * @param session
     * @return
     */
    @GetMapping(value = "/auth/list/{pageNum}/{pageSize}")
    public Result findListPage(@PathVariable("pageNum") Integer pageNum,
                               @PathVariable("pageSize") Integer pageSize,
                               HttpSession session){

        UserInfo userInfo = (UserInfo) session.getAttribute("USER");
        PageInfo<UserFollowVo> pageInfo = userFollowService.findListPage(pageNum, pageSize, userInfo.getId());

        return Result.ok(pageInfo);
    }

    /**
     * 取消关注
     * @param id
     * @return
     */
    @GetMapping("/auth/cancelFollow/{id}")
    public Result cancelFollow(@PathVariable("id") Long id){
        //创建UserFollow对象
        UserFollow userFollow = new UserFollow();
        userFollow.setId(id);
        userFollow.setIsDeleted(1);
        //调用业务层的方法修改
        userFollowService.update(userFollow);
        return Result.ok();
    }
}
