package com.yafnds.handler;

import com.alibaba.fastjson.JSON;
import com.yafnds.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：全局异常处理类
 * <p>创建时间：2022/10/10/010 21:30
 *
 * @author yafnds
 * @version 1.0
 */
@ControllerAdvice
public class GeneralExceptionHandler {

    private static final String PAGE_ERROR = "common/error";

    /**
     * 拦截所有运行时异常
     *  如果是同步请求，将异常信息放入请求域，然后跳转到错误页面
     *  如果是异步请求，将异常信息加入 201 状态码，转换成 json 后返回
     * @param request
     * @param response
     * @param e
     * @return
     * @throws IOException
     */
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        // 1. 区别当前请求是同步请求还是异步请求
        if (request.getHeader("accept").contains("application/json")) {
            // 异步请求
            Result<Object> result = Result.build(null, 201, e.getMessage());
            response.getWriter().write(JSON.toJSONString(result));
            return null;
        } else {
            // 同步请求
            // 异常信息存储到请求域
            request.setAttribute("errorMessage", e.getMessage());
            // 返回异常页面的逻辑视图
            return PAGE_ERROR;
        }
    }
}
