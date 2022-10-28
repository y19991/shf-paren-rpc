package com.yafnds.service;

import com.yafnds.base.BaseService;
import com.yafnds.entity.HouseUser;

import java.util.List;

/**
 * 类描述：
 * <p>创建时间：2022/10/7/007 14:30
 *
 * @author yafnds
 * @version 1.0
 */

public interface HouseUserService extends BaseService<HouseUser> {

    /**
     * 根据 房源 id 查询 房东
     * @param houseId 房源 id
     * @return 房东列表
     */
    List<HouseUser> findHouseUserListByHouseId(Long houseId);
}
