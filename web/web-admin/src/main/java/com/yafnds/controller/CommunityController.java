package com.yafnds.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.yafnds.base.BaseController;
import com.yafnds.entity.Community;
import com.yafnds.entity.Dict;
import com.yafnds.service.CommunityService;
import com.yafnds.service.DictService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 类描述：小区管理模块 控制层
 * <p>创建时间：2022/10/5/005 21:29
 *
 * @author yafnds
 * @version 1.0
 */

@Controller
@RequestMapping("/community")
public class CommunityController extends BaseController {

    @Reference
    private CommunityService communityService;
    @Reference
    private DictService dictService;

    private static final String PAGE_INDEX = "community/index";
    private static final String PAGE_CREATE = "community/create";
    private static final String PAGE_EDIT = "community/edit";
    private final static String LIST_ACTION = "redirect:/community";

    /**
     * 小区管理模块主页 展示小区列表
     * @param filters
     * @param model
     * @return
     */
    @RequestMapping
    public String index(@RequestParam Map<String, Object> filters, Model model) {

        // 查询小区列表数据
        PageInfo<Community> communityPageInfo = communityService.findPage(filters);

        // 处理 areaId 和 plateId 为空的情况
        if (!filters.containsKey("areaId")) {
            filters.put("areaId", 0);
        }
        if (!filters.containsKey("plateId")) {
            filters.put("plateId", 0);
        }

        model.addAttribute("filters", filters);
        model.addAttribute("page", communityPageInfo);

        // 查询区域信息，并将结果放入 model
        getBeijingAreaList(model);

        return PAGE_INDEX;
    }

    /**
     * 跳转至新增小区页面
     */
    @RequestMapping("/create")
    public String create(Model model) {

        getBeijingAreaList(model);
        return PAGE_CREATE;
    }

    /**
     * 获取北京的区域信息列表
     * @param model
     */
    private void getBeijingAreaList(Model model) {
        List<Dict> areaList = dictService.findDictListByParentDictCode("beijing");
        model.addAttribute("areaList", areaList);
    }

    /**
     * 新增小区
     * @param community 要新增的小区信息
     * @return 成功信息
     */
    @PostMapping("/save")
    public String save(Community community, Model model) {
        communityService.insert(community);
        return successPage("添加小区成功", model);
    }

    /**
     * 跳转至修改小区页
     * @param id 要修改的小区 id
     */
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        // 查询小区信息
        Community community = communityService.findById(id);

        model.addAttribute("community", community);

        getBeijingAreaList(model);
        return PAGE_EDIT;
    }

    /**
     * 修改小区
     * @param community 修改后数据
     * @return 跳转至成功页
     */
    @PostMapping("/update")
    public String update(Community community,Model model){
        communityService.update(community);
        return successPage("修改小区信息成功", model);
    }

    /**
     * 删除小区
     * @param id 要删除的小区 id
     * @return 重定向访问首页
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        //调用业务层的方法根据id删除小区信息
        communityService.delete(id);
        //重定向访问首页
        return LIST_ACTION;
    }


}
