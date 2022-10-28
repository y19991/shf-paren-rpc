package com.yafnds.interceptor;

import com.yafnds.result.Result;
import com.yafnds.result.ResultCodeEnum;
import com.yafnds.util.WebUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 类描述：
 * <p>创建时间：2022/10/17/017 18:24
 *
 * @author yafnds
 * @version 1.0
 */

public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 在Controller方法执行前调用
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 判断是否已登录
        if (!(request.getSession().getAttribute("USER") == null)) {
            return true;
        }

        // 默认未登录
        WebUtil.writeJSON(response, Result.build("未登录", ResultCodeEnum.LOGIN_AUTH));
        return false;
    }
}
