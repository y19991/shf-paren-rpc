package com.yafnds.mapper;

import com.yafnds.base.BaseMapper;
import com.yafnds.entity.Community;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 类描述：
 * <p>创建时间：2022/10/6/006 15:27
 *
 * @author yafnds
 * @version 1.0
 */
@Repository
public interface CommunityMapper extends BaseMapper<Community> {

    /**
     * 查询全部小区信息
     */
    List<Community> findAll();
}
