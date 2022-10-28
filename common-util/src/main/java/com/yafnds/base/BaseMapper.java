package com.yafnds.base;

import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * 类描述：
 * <p>创建时间：2022/10/4/004 19:50
 *
 * @author yafnds
 * @version 1.0
 */

public interface BaseMapper<T> {

    /**
     * 保存一个实体
     * @param t 需要保存的实体类
     */
    void insert(T t);

    /**
     * 批量添加
     * @param list 需要添加的实体类列表
     */
    void insertBatch(List<T> list);

    /**
     * 根据 id 删除。
     * 逻辑删除，只修改数据库中的删除标识，不真正删除数据
     * @param id 需要删除的实体的标识 ID
     */
    void delete(Long id);

    /**
     * 更新一个实体
     * @param t 需要更新的实体类
     */
    void update(T t);

    /**
     * 根据标识 ID 查询对应实体
     * @param id 标识 ID
     * @return 该 ID 对应的实体信息
     */
    T findById(Long id);

    /**
     * 分页查询
     * @param filters 查询条件
     * @return 分页查询结果
     */
    Page<T> findPage(Map<String, Object> filters);
}
