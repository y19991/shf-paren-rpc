package com.yafnds.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.yafnds.base.BaseMapper;
import com.yafnds.base.BaseServiceImpl;
import com.yafnds.entity.Community;
import com.yafnds.mapper.CommunityMapper;
import com.yafnds.mapper.HouseMapper;
import com.yafnds.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 类描述：小区管理模块 服务层实现类
 * <p>创建时间：2022/10/6/006 15:24
 *
 * @author yafnds
 * @version 1.0
 */
@Service(interfaceClass = CommunityService.class)
public class CommunityServiceImpl extends BaseServiceImpl<Community> implements CommunityService {

    @Autowired
    private CommunityMapper communityMapper;
    @Autowired
    private HouseMapper houseMapper;

    @Override
    protected BaseMapper<Community> getEntityMapper() {
        return communityMapper;
    }

    /**
     * 删除小区
     * @param id 要删除的数据的 标识 ID
     */
    @Override
    public void delete(Long id) {
        //判断:如果该小区下有房源,则不能删除
        Integer count = houseMapper.findCountByCommunityId(id);
        if (count > 0) {
            //说明该小区下有房源，不能删除
            throw new RuntimeException("该小区下有房源，不能删除");
        }
        //该小区下没有房源，可以删除
        super.delete(id);
    }

    /**
     * 查询全部小区信息
     */
    @Override
    public List<Community> findAll() {
        return communityMapper.findAll();
    }
}
