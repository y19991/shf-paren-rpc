package com.yafnds.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：
 * <p>创建时间：2022/10/20/020 14:17
 *
 * @author yafnds
 * @version 1.0
 */
@Component
public class ShfAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       AccessDeniedException e) throws IOException, ServletException {

        //你可以编写任何代码处理权限不够的情况: httpServletRequest请求对象,httpServletResponse响应对象,e异常对象
        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/auth");
    }
}
