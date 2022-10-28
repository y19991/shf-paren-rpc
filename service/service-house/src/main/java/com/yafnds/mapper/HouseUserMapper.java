package com.yafnds.mapper;

import com.yafnds.base.BaseMapper;
import com.yafnds.entity.HouseUser;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 类描述：房东 持久层接口
 * <p>创建时间：2022/10/7/007 14:27
 *
 * @author yafnds
 * @version 1.0
 */
@Repository
public interface HouseUserMapper extends BaseMapper<HouseUser> {

    /**
     * 根据 房源 id 查询 房东
     * @param houseId 房源 id
     * @return 房东列表
     */
    List<HouseUser> findHouseUserListByHouseId(Long houseId);

}
