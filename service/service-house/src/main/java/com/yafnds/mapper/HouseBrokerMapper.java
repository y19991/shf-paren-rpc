package com.yafnds.mapper;

import com.yafnds.base.BaseMapper;
import com.yafnds.entity.HouseBroker;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 类描述：房源经纪人 持久层接口
 * <p>创建时间：2022/10/7/007 14:19
 *
 * @author yafnds
 * @version 1.0
 */
@Repository
public interface HouseBrokerMapper extends BaseMapper<HouseBroker> {

    /**
     * 根据 房源 id 查询 房源经纪人 信息
     * @param houseId 房源 id
     * @return 房源经纪人列表
     */
    List<HouseBroker> findHouseBrokerListByHouseId(Long houseId);
}
