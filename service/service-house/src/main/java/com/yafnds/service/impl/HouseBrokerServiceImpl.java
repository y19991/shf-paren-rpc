package com.yafnds.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.yafnds.base.BaseMapper;
import com.yafnds.base.BaseServiceImpl;
import com.yafnds.entity.HouseBroker;
import com.yafnds.mapper.HouseBrokerMapper;
import com.yafnds.service.HouseBrokerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 类描述：
 * <p>创建时间：2022/10/7/007 14:24
 *
 * @author yafnds
 * @version 1.0
 */
@Service(interfaceClass = HouseBrokerService.class)
public class HouseBrokerServiceImpl extends BaseServiceImpl<HouseBroker> implements HouseBrokerService {

    @Autowired
    private HouseBrokerMapper houseBrokerMapper;

    @Override
    protected BaseMapper<HouseBroker> getEntityMapper() {
        return houseBrokerMapper;
    }

    /**
     * 根据 房源 id 查询 房源经纪人 信息
     * @param houseId 房源 id
     * @return 房源经纪人列表
     */
    @Override
    public List<HouseBroker> findHouseBrokerListByHouseId(Long houseId) {
        return houseBrokerMapper.findHouseBrokerListByHouseId(houseId);
    }
}
