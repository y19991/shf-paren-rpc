package com.yafnds.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.yafnds.base.BaseController;
import com.yafnds.en.DictCode;
import com.yafnds.en.HouseImageType;
import com.yafnds.en.HouseStatus;
import com.yafnds.entity.*;
import com.yafnds.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 类描述：房源管理模块 表现层
 * <p>创建时间：2022/10/7/007 8:58
 *
 * @author yafnds
 * @version 1.0
 */
@Controller
@RequestMapping("/house")
public class HouseController extends BaseController {

    @Reference
    private HouseService houseService;
    @Reference
    private CommunityService communityService;
    @Reference
    private DictService dictService;
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private HouseUserService houseUserService;

    private static final String PAGE_INDEX = "house/index";
    private static final String PAGE_CREATE = "house/create";
    private static final String PAGE_EDIT = "house/edit";
    private static final String LIST_ACTION = "redirect:/house";
    private static final String PAGE_SHOW = "house/show";

    /**
     * 房源管理模块 主页 分页查询
     *
     * @param filters 查询条件
     * @param model
     * @return 跳转至房源管理模块主页
     */
    @PreAuthorize("hasAuthority('house.show')")
    @RequestMapping
    public String index(@RequestParam Map<String, Object> filters, Model model) {

        PageInfo<House> pageInfo = houseService.findPage(filters);

        model.addAttribute("page", pageInfo);
        model.addAttribute("filters", filters);

        // 查询所有小区、以及字典里的各种列表存储到请求域
        saveAllDictToRequestScope(model);

        return PAGE_INDEX;
    }

    /**
     * 查询所有字典信息以及小区列表并存储到请求域
     */
    private void saveAllDictToRequestScope(Model model) {
        // 1. 查询所有小区，并存储到请求域
        model.addAttribute("communityList", communityService.findAll());
        // 2. 查询所有户型:根据父节点的dictCode查询子节点列表，并存储到请求域
        model.addAttribute("houseTypeList", dictService.findDictListByParentDictCode(DictCode.HOUSETYPE.code));
        // 3. 查询所有楼层，并存储到请求域
        model.addAttribute("floorList", dictService.findDictListByParentDictCode(DictCode.FLOOR.code));
        // 4. 查询所有建筑结构，并存储到请求域
        model.addAttribute("buildStructureList", dictService.findDictListByParentDictCode(DictCode.BUILDSTRUCTURE.code));
        // 5. 查询所有朝向，并存储到请求域
        model.addAttribute("directionList", dictService.findDictListByParentDictCode(DictCode.DIRECTION.code));
        // 6. 查询所有装修情况，并存储到请求域
        model.addAttribute("decorationList", dictService.findDictListByParentDictCode(DictCode.DECORATION.code));
        // 7. 查询所有房屋用途，并存储到请求域
        model.addAttribute("houseUseList", dictService.findDictListByParentDictCode(DictCode.HOUSEUSE.code));
    }

    /**
     * 跳转至新增
     */
    @PreAuthorize("hasAuthority('house.create')")
    @RequestMapping("/create")
    public String create(Model model){
        // 查询房屋初始化信息存储到 model 中
        saveAllDictToRequestScope(model);
        return PAGE_CREATE;
    }

    /**
     * 新增房源
     * @param house 新增的房源数据
     * @return 跳转至成功页面
     */
    @PreAuthorize("hasAuthority(house.create)")
    @PostMapping("/save")
    public String save(House house,Model model){
        // 未发布
        house.setStatus(HouseStatus.UNPUBLISHED.getCode());
        houseService.insert(house);
        return successPage("添加房源信息成功", model);
    }

    /**
     * 跳转至修改页
     * @param id 要修改的房源的 id
     */
    @PreAuthorize("hasAuthority('house.edit')")
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        House house = houseService.findById(id);
        model.addAttribute("house",house);

        saveAllDictToRequestScope(model);

        return PAGE_EDIT;
    }

    /**
     * 修改房源
     * @param house 要修改的房源的 id
     * @return 跳转至成功页面
     */
    @PreAuthorize("hasAuthority('house.edit')")
    @PostMapping("/update")
    public String update(House house,Model model){
        houseService.update(house);
        return successPage("修改房源信息成功", model);
    }

    /**
     * 删除房源
     * @param id 要删除的房源的 id
     * @return 跳转至房源管理模块主页
     */
    @PreAuthorize("hasAuthority('house.delete')")
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        houseService.delete(id);
        return LIST_ACTION;
    }

    /**
     * 发布&取消发布 房源
     * @param id 房源 id
     * @param status 房源状态
     * @return 跳转至房源管理主页面
     */
    @PreAuthorize("hasAuthority('house.publish')")
    @GetMapping("/publish/{id}/{status}")
    public String publish(@PathVariable("id") Long id, @PathVariable("status") Integer status){
        House house = new House();
        house.setId(id);
        house.setStatus(status);
        houseService.update(house);
        return LIST_ACTION;
    }

    /**
     * 详情
     * @param houseId 房源 id
     * @return 跳转至详情页
     */
    @PreAuthorize("hasAuthority('house.show')")
    @GetMapping("/{houseId}")
    public String detail(@PathVariable("houseId") Long houseId, Model model){
        //1. 根据id查询房源详情
        House house = houseService.findById(houseId);
        //2. 根据小区id查询小区详情
        Community community = communityService.findById(house.getCommunityId());
        //3. 根据房源id查询房源的房源图片列表
        List<HouseImage> houseImage1List = houseImageService.findHouseImageList(houseId, HouseImageType.HOUSE_SOURCE.getCode());
        //4. 根据房源id查询房源的房产图片列表
        List<HouseImage> houseImage2List = houseImageService.findHouseImageList(houseId, HouseImageType.HOUSE_PROPERTY.getCode());
        //5. 根据房源id查询房源的经纪人列表
        List<HouseBroker> houseBrokerList = houseBrokerService.findHouseBrokerListByHouseId(houseId);
        //6. 根据房源id查询房源的房东列表
        List<HouseUser> houseUserList = houseUserService.findHouseUserListByHouseId(houseId);

        //将上述查询到的内容存储到请求域
        model.addAttribute("house",house);
        model.addAttribute("community",community);
        model.addAttribute("houseImage1List",houseImage1List);
        model.addAttribute("houseImage2List",houseImage2List);
        model.addAttribute("houseBrokerList",houseBrokerList);
        model.addAttribute("houseUserList",houseUserList);

        return PAGE_SHOW;
    }
}
