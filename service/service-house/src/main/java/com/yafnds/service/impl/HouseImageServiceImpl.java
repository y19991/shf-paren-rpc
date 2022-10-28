package com.yafnds.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.yafnds.base.BaseMapper;
import com.yafnds.base.BaseServiceImpl;
import com.yafnds.entity.HouseImage;
import com.yafnds.mapper.HouseImageMapper;
import com.yafnds.service.HouseImageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 类描述：
 * <p>创建时间：2022/10/7/007 14:08
 *
 * @author yafnds
 * @version 1.0
 */
@Service(interfaceClass = HouseImageService.class)
public class HouseImageServiceImpl extends BaseServiceImpl<HouseImage> implements HouseImageService {

    @Autowired
    private HouseImageMapper houseImageMapper;

    @Override
    protected BaseMapper<HouseImage> getEntityMapper() {
        return houseImageMapper;
    }

    /**
     * 根据 房源 id 和 房源类型 查找对应的房屋图片
     * @param houseId 房源 id
     * @param type 房源类型
     * @return 房源图片列表
     */
    @Override
    public List<HouseImage> findHouseImageList(Long houseId, Integer type) {
        return houseImageMapper.findHouseImageList(houseId, type);
    }
}
