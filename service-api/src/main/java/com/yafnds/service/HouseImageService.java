package com.yafnds.service;

import com.yafnds.base.BaseService;
import com.yafnds.entity.HouseImage;

import java.util.List;

/**
 * 类描述：房源图片 业务层接口
 * <p>创建时间：2022/10/7/007 11:41
 *
 * @author yafnds
 * @version 1.0
 */

public interface HouseImageService extends BaseService<HouseImage> {

    /**
     * 根据 房源 id 和 房源类型 查找对应的房屋图片
     * @param houseId 房源 id
     * @param type 房源类型
     * @return 房源图片列表
     */
    List<HouseImage> findHouseImageList(Long houseId, Integer type);
}
