package com.yafnds.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yafnds.base.BaseController;
import com.yafnds.entity.Dict;
import com.yafnds.result.Result;
import com.yafnds.service.DictService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 类描述：数字字典
 * <p>创建时间：2022/10/5/005 19:03
 *
 * @author yafnds
 * @version 1.0
 */

@RestController
@RequestMapping("/dict")
public class DictController extends BaseController {

    @Reference
    private DictService dictService;

    /**
     * 查询子节点的树形结构数据
     * @param id 父节点 id
     * @return 返回成功数据
     */
    @GetMapping("/findZnodes")
    public Result findZNodes(@RequestParam(value = "id", defaultValue = "0") Long id) {
        List<Map<String, Object>> zNodes = dictService.findZNodes(id);
        return Result.ok(zNodes);
    }

    /**
     * 根据父节点 id 查询子节点列表
     * @param parentId
     * @return
     */
    @RequestMapping("/findDictListByParentId/{parentId}")
    public Result findDictListByParentId(@PathVariable Long parentId) {
        List<Dict> dictList = dictService.findDictListByParentId(parentId);
        return Result.ok(dictList);
    }

}
