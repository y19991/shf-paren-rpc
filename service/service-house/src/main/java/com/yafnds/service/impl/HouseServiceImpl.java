package com.yafnds.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yafnds.base.BaseMapper;
import com.yafnds.base.BaseServiceImpl;
import com.yafnds.entity.House;
import com.yafnds.entity.vo.HouseQueryVo;
import com.yafnds.entity.vo.HouseVo;
import com.yafnds.mapper.HouseMapper;
import com.yafnds.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 类描述：
 * <p>创建时间：2022/10/7/007 9:03
 * @author yafnds
 * @version 1.0
 */
@Service(interfaceClass = HouseService.class)
public class HouseServiceImpl extends BaseServiceImpl<House> implements HouseService {

    @Autowired
    private HouseMapper houseMapper;

    @Override
    protected BaseMapper<House> getEntityMapper() {
        return houseMapper;
    }

    /**
     * 分页查询（前台页面数据）
     * @param pageNum 第几页
     * @param pageSize 单页条数
     * @param houseQueryVo 房源数据（前台页面数据）
     * @return 分页后的数据列表
     */
    @Override
    public PageInfo<HouseVo> findListPage(Integer pageNum, Integer pageSize, HouseQueryVo houseQueryVo) {

        PageHelper.startPage(pageNum, pageSize);
        Page<HouseVo> houseVoList =  houseMapper.findListPage(houseQueryVo);

        return new PageInfo<>(houseVoList, 10);
    }
}
