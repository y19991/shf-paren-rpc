package com.yafnds.mapper;


import com.yafnds.base.BaseMapper;
import com.yafnds.entity.Dict;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 类描述：
 * <p>创建时间：2022/10/5/005 18:54
 *
 * @author yafnds
 * @version 1.0
 */
@Repository
public interface DictMapper extends BaseMapper<Dict> {

    /**
     * 根据父节点 id 查询子节点列表
     * @param id 父节点 id
     * @return 子节点列表
     */
    List<Dict> findListByParentId(Long id);

    /**
     * 统计该父节点 id 下子节点的数量
     * @param id 父节点 id
     * @return 所属的子节点的数量
     */
    Integer countIdByParentId(Long id);

    /**
     * 根据父节点的 dictCode 查询子节点列表
     * @param parentDictCode 父节点的 dictCode
     * @return 子节点列表
     */
    List<Dict> findDictListByParentDictCode(String parentDictCode);
}
