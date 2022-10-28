package com.yafnds.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yafnds.util.CastUtil;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 类描述：
 * <p>创建时间：2022/10/4/004 20:44
 *
 * @author yafnds
 * @version 1.0
 */

public abstract class BaseServiceImpl<T> {

    /**
     * 获取 mapper 对象
     */
    protected abstract BaseMapper<T> getEntityMapper();

    /**
     * 保存
     * @param t 要保存的实体
     */
    public void insert(T t) {
        getEntityMapper().insert(t);
    }

    /**
     * 删除
     * @param id 要删除的数据的 标识 ID
     */
    public void delete(Long id) {
        getEntityMapper().delete(id);
    }

    /**
     * 修改
     * @param t 要修改的数据
     */
    public void update(T t) {
        getEntityMapper().update(t);
    }

    /**
     * 根据 标识 ID 查找实体
     * @param id 标识 ID
     * @return ID 对应的实体
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public T findById(Long id) {
        return getEntityMapper().findById(id);
    }

    /**
     * 分页查询
     * @param filters 查询条件
     * @return 分页查询结果
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public PageInfo<T> findPage(Map<String, Object> filters) {

        // 当前页数
        int pageNum = CastUtil.castInt(filters.get("pageNum"), 1);
        // 每页显示的记录条数
        int pageSize = CastUtil.castInt(filters.get("pageSize"), 10);
        PageHelper.startPage(pageNum, pageSize);

        return new PageInfo<>(getEntityMapper().findPage(filters), 10);
    }
}
