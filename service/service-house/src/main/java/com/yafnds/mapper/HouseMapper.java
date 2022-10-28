package com.yafnds.mapper;

import com.github.pagehelper.Page;
import com.yafnds.base.BaseMapper;
import com.yafnds.entity.House;
import com.yafnds.entity.vo.HouseQueryVo;
import com.yafnds.entity.vo.HouseVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 类描述：房源模块 持久层接口
 * <p>创建时间：2022/10/6/006 19:40
 *
 * @author yafnds
 * @version 1.0
 */
@Repository
public interface HouseMapper extends BaseMapper<House> {

    /**
     * 根据小区 id 查询房源数量
     * @param communityId 小区 id
     * @return 房源数量
     */
    Integer findCountByCommunityId(Long communityId);

    /**
     * 分页查询（前台页面）
     * @param houseQueryVo 房源数据（前台页面数据）
     * @return 分页后的数据列表
     */
    Page<HouseVo> findListPage(HouseQueryVo houseQueryVo);

}
