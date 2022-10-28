package com.yafnds.service;

import com.yafnds.base.BaseService;
import com.yafnds.entity.Community;

import java.util.List;

/**
 * 类描述：小区管理模块 服务层接口
 * <p>创建时间：2022/10/6/006 15:20
 *
 * @author yafnds
 * @version 1.0
 */

public interface CommunityService extends BaseService<Community> {

    /**
     * 查询全部小区信息
     */
    List<Community> findAll();
}
