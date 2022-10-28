package com.yafnds.service.impl;

import com.yafnds.base.BaseMapper;
import com.yafnds.base.BaseServiceImpl;
import com.yafnds.entity.Dict;
import com.yafnds.mapper.DictMapper;
import com.yafnds.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
//import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：
 * <p>创建时间：2022/10/5/005 19:27
 *
 * @author yafnds
 * @version 1.0
 */

@Service(interfaceClass = DictService.class)
//@Service
public class DictServiceImpl extends BaseServiceImpl<Dict> implements DictService {

    @Autowired
    private DictMapper dictMapper;

    @Override
    protected BaseMapper<Dict> getEntityMapper() {
        return dictMapper;
    }

    /**
     * 查询子节点的树形结构数据
     * @param id 父节点 id
     * @return 子节点列表
     */
    @Override
    public List<Map<String, Object>> findZNodes(Long id) {

        // 1. 根据父节点 id 查询对应子节点列表
        List<Dict> dictList = dictMapper.findListByParentId(id);

        // 2. 用 stream 流将 List<Dict> 转换为 List<Map<String, Object>>
        List<Map<String, Object>> zNodes = dictList.stream()
                .map(dict -> {
                    Map<String, Object> zNode = new HashMap<>();
                    zNode.put("id", dict.getId());
                    zNode.put("name", dict.getName());
                    // 判断该节点是否是父节点，并将结果放入 map
                    zNode.put("isParent", dictMapper.countIdByParentId(dict.getId()) > 0);
                    return zNode;
                })
                .collect(Collectors.toList());

        return zNodes;
    }

    /**
     * 根据父节点的 dictCode 查询子节点列表
     * @param parentDictCode 父节点的 dictCode
     * @return 子节点列表
     */
    @Override
    public List<Dict> findDictListByParentDictCode(String parentDictCode) {
        return dictMapper.findDictListByParentDictCode(parentDictCode);
    }

    /**
     * 根据父节点 id 查询子节点列表
     * @param parentId 父节点 id
     * @return 子节点列表
     */
    @Override
    public List<Dict> findDictListByParentId(Long parentId) {
        return dictMapper.findListByParentId(parentId);
    }
}
