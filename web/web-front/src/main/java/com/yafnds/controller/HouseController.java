package com.yafnds.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.yafnds.entity.*;
import com.yafnds.entity.vo.HouseQueryVo;
import com.yafnds.entity.vo.HouseVo;
import com.yafnds.result.Result;
import com.yafnds.service.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：前台页面 房源数据
 * <p>创建时间：2022/10/13/013 15:02
 *
 * @author yafnds
 * @version 1.0
 */
@RestController
@RequestMapping("/house")
public class HouseController {

    @Reference
    private HouseService houseService;
    @Reference
    private CommunityService communityService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private UserFollowService userFollowService;

    /**
     * 分页查询 前台页面房源数据
     * @param houseQueryVo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @PostMapping("/list/{pageNum}/{pageSize}")
    public Result list(@PathVariable Integer pageNum,
                       @PathVariable Integer pageSize,
                       @RequestBody HouseQueryVo houseQueryVo){
        //调用业务层的方法查询前台房源的分页数据
        PageInfo<HouseVo> pageInfo = houseService.findListPage(pageNum, pageSize, houseQueryVo);
        return Result.ok(pageInfo);
    }

    /**
     * 房源详情
     * @param id 房源 id
     * @param session
     * @return
     */
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id, HttpSession session){
        // 1. 查询客户端需要的数据，并且存储到Map对象中
        Map<String,Object> responseMap = new HashMap<>();
        // 1.1 根据id查询房源信息,存储到responseMap中
        House house = houseService.findById(id);
        responseMap.put("house",house);
        // 1.2 查询小区信息,存储到responseMap中
        Community community = communityService.findById(house.getCommunityId());
        responseMap.put("community",community);
        // 1.3 查询房源的经纪人列表,存储到responseMap中
        List<HouseBroker> houseBrokerList = houseBrokerService.findHouseBrokerListByHouseId(id);
        responseMap.put("houseBrokerList",houseBrokerList);
        // 1.4 查询房源的房源图片列表,存储到responseMap中
        List<HouseImage> houseImage1List = houseImageService.findHouseImageList(id, 1);
        responseMap.put("houseImage1List",houseImage1List);
        // 1.5 查询用户是否已关注该房源,存储到responseMap中
        boolean isFollow = false;
        // 1.5.1 从session中获取当前登录的用户信息
        UserInfo userInfo = (UserInfo) session.getAttribute("USER");
        // 1.5.2 如果当前未登录，那么isFollow肯定是false
        if (userInfo != null) {
            // 1.5.3 如果当前已登录，则判断当前用户是否已关注当前房源
            UserFollow userFollow = userFollowService.findByUserIdAndHouseId(userInfo.getId(), id);

            isFollow = (userFollow != null && userFollow.getIsDeleted() == 0);
        }

        responseMap.put("isFollow",isFollow);
        // 2. 将响应数据封装到Result中，并返回
        return Result.ok(responseMap);
    }


}
