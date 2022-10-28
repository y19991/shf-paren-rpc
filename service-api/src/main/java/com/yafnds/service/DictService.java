package com.yafnds.service;

import com.yafnds.base.BaseService;
import com.yafnds.entity.Dict;

import java.util.List;
import java.util.Map;

/**
 * 类描述：数字字典 服务层接口
 * <p>创建时间：2022/10/5/005 19:22
 *
 * @author yafnds
 * @version 1.0
 */
public interface DictService extends BaseService<Dict> {

    /**
     * 查询子节点的树形结构数据
     * @param id 父节点 id
     * @return 子节点列表
     */
    List<Map<String, Object>> findZNodes(Long id);

    /**
     * 根据父节点的 dictCode 查询子节点列表
     * @param parentDictCode 父节点的 dictCode
     * @return 子节点列表
     */
    List<Dict> findDictListByParentDictCode(String parentDictCode);

    /**
     * 根据父节点 id 查询子节点列表
     * @param parentId 父节点 id
     * @return 子节点列表
     */
    List<Dict> findDictListByParentId(Long parentId);

}
