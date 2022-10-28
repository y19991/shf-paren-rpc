package com.yafnds.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yafnds.entity.House;
import com.yafnds.entity.HouseImage;
import com.yafnds.result.Result;
import com.yafnds.service.HouseImageService;
import com.yafnds.service.HouseService;
import com.yafnds.util.FileUtil;
import com.yafnds.util.QiniuUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 类描述：
 * <p>创建时间：2022/10/10/010 16:24
 *
 * @author yafnds
 * @version 1.0
 */

@Controller
@RequestMapping("/houseImage")
public class HouseImageController {

    @Reference
    private HouseImageService houseImageService;
    @Reference
    private HouseService houseService;

    private final static String PAGE_UPLOED_SHOW = "house/upload";
    private static final String SHOW_ACTION = "redirect:/house/";

    /**
     * 跳转到上传页面
     * @param houseId 房源 id
     * @param type 房源类型
     */
    @GetMapping("uploadShow/{houseId}/{type}")
    public String uploadShow(@PathVariable Long houseId, @PathVariable Integer type, Model model) {

        model.addAttribute("houseId", houseId);
        model.addAttribute("type", type);

        return PAGE_UPLOED_SHOW;
    }

    /**
     * 房源图片上传(异步)
     * @param houseId   房源 id
     * @param type      房源类型
     * @param fileList  要上传的图片列表
     * @return 成功的 JSON 信息
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/upload/{houseId}/{type}")
    public Result upload(@PathVariable Long houseId,
                         @PathVariable Integer type,
                         @RequestParam("file")List<MultipartFile> fileList) throws IOException {

        // 1. 遍历上传的文件 files
        if (!CollectionUtils.isEmpty(fileList)) {
            for (int i = 0; i < fileList.size(); i++) {

                MultipartFile multipartFile = fileList.get(i);

                // 2. 将图片上传至七牛云
                // 2.1 获取客户端上传的文件的文件名，并据此生成唯一的文件名
                String uuidName = FileUtil.getUUIDName(multipartFile.getOriginalFilename());
                // 2.2 规范文件存储（指定存储目录，按日期分类）
                String dateDirPath = FileUtil.getDateDirPath();
                String fileName = dateDirPath + uuidName;
                // 2.3 上传文件到七牛云
                QiniuUtils.upload2Qiniu(multipartFile.getBytes(), fileName);
                // 2.4 获取该文件的访问路径
                String url = QiniuUtils.getUrl(fileName);

                // 3. 将 “上传的图片的访问路径” 、“上传的图片在七牛云中的路径”、“上传的图片对应的房源id” 保存到 hse_house_image 表中
                HouseImage houseImage = new HouseImage();
                houseImage.setHouseId(houseId);
                houseImage.setImageName(fileName);
                houseImage.setImageUrl(url);
                houseImage.setType(type);
                houseImageService.insert(houseImage);

                /*
                    4. 判断当前房源是否有默认图片
                        如果没有，就把当前上传的第一张图片作为默认图片
                 */
                if (i == 0) {
                    // 4.1 根据 houseId 查询房源信息
                    House house = houseService.findById(houseId);
                    // 4.2 获取房源的默认图片的路径
                    String defaultImageUrl = house.getDefaultImageUrl();
                    // 4.3 判断其是否存在
                    if (StringUtils.isEmpty(defaultImageUrl) || "null".equalsIgnoreCase(defaultImageUrl)) {
                        // 4.4 如果没有默认图片，则把当前上传的第一张图片作为默认图片
                        house.setDefaultImageUrl(url);
                        // 4.5 更新数据
                        houseService.update(house);
                    }
                }
            }
        }
        return Result.ok();
    }

    /**
     * 图片删除
     * @param houseId 房源 id
     * @param id      房源图片 id
     * @return
     */
    @GetMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable Long houseId, @PathVariable Long id) {

        // 1. 根据 id 查出要删除的图片
        HouseImage houseImage = houseImageService.findById(id);
        // 2. 从七牛云中删除对应图片
        QiniuUtils.deleteFileFromQiniu(houseImage.getImageName());
        // 3. 从数据库表中删除对应图片
        houseImageService.delete(id);
        // 4. 如果这张图片刚好是房源的默认图片，那么修改默认图片为空
        // 4.1 根据 房源 id 查询房源信息
        House house = houseService.findById(houseId);
        // 判断当前图片是否是默认图片
        if (houseImage.getImageUrl().equals(house.getDefaultImageUrl())) {
            house.setDefaultImageUrl("null");
            houseService.update(house);
        }

        // 重新显示房源的详情页
        return SHOW_ACTION;

    }

}
