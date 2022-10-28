package com.yafnds.service;

import com.github.pagehelper.PageInfo;
import com.yafnds.base.BaseService;
import com.yafnds.entity.House;
import com.yafnds.entity.vo.HouseQueryVo;
import com.yafnds.entity.vo.HouseVo;

/**
 * 类描述：
 * <p>创建时间：2022/10/7/007 9:01
 *
 * @author yafnds
 * @version 1.0
 */

public interface HouseService extends BaseService<House> {

    /**
     * 分页查询列表（前台页面）
     * @param pageNum 第几页
     * @param pageSize 单页条数
     * @param houseQueryVo 房源数据（前台页面数据）
     * @return 分页后的数据列表
     */
    PageInfo<HouseVo> findListPage(Integer pageNum, Integer pageSize, HouseQueryVo houseQueryVo);

}
