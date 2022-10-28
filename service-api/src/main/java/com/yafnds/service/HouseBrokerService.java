package com.yafnds.service;

import com.yafnds.base.BaseService;
import com.yafnds.entity.HouseBroker;

import java.util.List;

/**
 * 类描述：房源经纪人 业务层接口
 * <p>创建时间：2022/10/7/007 14:22
 *
 * @author yafnds
 * @version 1.0
 */

public interface HouseBrokerService extends BaseService<HouseBroker> {

    /**
     * 根据 房源 id 查询 房源经纪人 信息
     * @param houseId 房源 id
     * @return 房源经纪人列表
     */
    List<HouseBroker> findHouseBrokerListByHouseId(Long houseId);
}
