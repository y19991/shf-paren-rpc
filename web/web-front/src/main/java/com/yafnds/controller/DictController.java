package com.yafnds.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yafnds.entity.Dict;
import com.yafnds.result.Result;
import com.yafnds.service.DictService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类描述：数字字典（前台页面）
 * <p>创建时间：2022/10/13/013 16:58
 *
 * @author yafnds
 * @version 1.0
 */

@RestController
@RequestMapping("/dict")
public class DictController {

    @Reference
    private DictService dictService;

    /**
     * 根据父字典代码查询字典列表
     * @param dictCode 父字典代码
     * @return 成功信息带字典列表
     */
    @GetMapping("/findDictListByParentDictCode/{dictCode}")
    public Result findDictListByParentDictCode(@PathVariable String dictCode) {

        List<Dict> dictList = dictService.findDictListByParentDictCode(dictCode);
        return Result.ok(dictList);
    }

    /**
     * 根据父字典id查询字典列表
     * @param parentId 父字典id
     * @return 成功信息带字典列表
     */
    @GetMapping("/findDictListByParentId/{parentId}")
    public Result findDictListByParentId(@PathVariable Long parentId) {

        List<Dict> dictList = dictService.findDictListByParentId(parentId);
        return Result.ok(dictList);
    }

}
