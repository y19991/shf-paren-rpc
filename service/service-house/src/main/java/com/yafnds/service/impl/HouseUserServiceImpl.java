package com.yafnds.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.yafnds.base.BaseMapper;
import com.yafnds.base.BaseServiceImpl;
import com.yafnds.entity.HouseUser;
import com.yafnds.mapper.HouseUserMapper;
import com.yafnds.service.HouseUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 类描述：
 * <p>创建时间：2022/10/7/007 14:30
 *
 * @author yafnds
 * @version 1.0
 */
@Service(interfaceClass = HouseUserService.class)
public class HouseUserServiceImpl extends BaseServiceImpl<HouseUser> implements HouseUserService {

    @Autowired
    private HouseUserMapper houseUserMapper;

    @Override
    protected BaseMapper<HouseUser> getEntityMapper() {
        return houseUserMapper;
    }

    /**
     * 根据 房源 id 查询 房东
     * @param houseId 房源 id
     * @return 房东列表
     */
    @Override
    public List<HouseUser> findHouseUserListByHouseId(Long houseId) {
        return houseUserMapper.findHouseUserListByHouseId(houseId);
    }
}
