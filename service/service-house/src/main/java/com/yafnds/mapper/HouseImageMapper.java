package com.yafnds.mapper;

import com.yafnds.base.BaseMapper;
import com.yafnds.entity.HouseImage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 类描述：
 * <p>创建时间：2022/10/7/007 11:37
 *
 * @author yafnds
 * @version 1.0
 */
@Repository
public interface HouseImageMapper extends BaseMapper<HouseImage> {

    /**
     * 根据 房源 id 和 房源类型 查找对应的房屋图片
     * @param houseId 房源 id
     * @param type 房源类型
     * @return 房源图片列表
     */
    List<HouseImage> findHouseImageList(@Param("houseId") Long houseId, @Param("type") Integer type);
}
