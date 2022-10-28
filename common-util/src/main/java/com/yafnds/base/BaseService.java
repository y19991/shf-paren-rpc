package com.yafnds.base;

import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * 类描述：
 * <p>创建时间：2022/10/4/004 20:35
 *
 * @author yafnds
 * @version 1.0
 */

public interface BaseService<T> {

    /**
     * 保存
     * @param t 要保存的实体类
     */
    void insert(T t);

    /**
     * 删除
     * @param id 要删除的数据的 标识 ID
     */
    void delete(Long id);

    /**
     * 修改
     * @param t 要修改的数据
     */
    void update(T t);

    /**
     * 根据 标识 ID 查找实体
     * @param id 标识 ID
     * @return ID 对应的实体
     */
    T findById(Long id);

    /**
     * 分页查询
     * @param filters 查询条件
     * @return 分页查询结果
     */
    PageInfo<T> findPage(Map<String, Object> filters);

}
