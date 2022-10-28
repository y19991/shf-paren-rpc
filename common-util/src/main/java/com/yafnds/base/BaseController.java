package com.yafnds.base;

import org.springframework.ui.Model;

/**
 * 类描述：
 * <p>创建时间：2022/10/4/004 21:06
 *
 * @author yafnds
 * @version 1.0
 */

public class BaseController {

    private static final String PAGE_SUCCESS = "common/successPage";

    /**
     * 跳转至成功页面
     * @param successMessage 成功信息
     */
    public String successPage(String successMessage, Model model) {
        model.addAttribute("messagePage", successMessage);
        return PAGE_SUCCESS;
    }

}
