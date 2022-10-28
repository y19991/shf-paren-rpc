package com.yafnds.controller;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.yafnds.base.BaseController;
import com.yafnds.entity.UserInfo;
import com.yafnds.entity.vo.LoginVo;
import com.yafnds.entity.vo.RegisterVo;
import com.yafnds.result.Result;
import com.yafnds.result.ResultCodeEnum;
import com.yafnds.service.UserInfoService;
import com.yafnds.util.MD5;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：用户信息 控制层
 * <p>创建时间：2022/10/14/014 15:07
 *
 * @author yafnds
 * @version 1.0
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Reference
    private UserInfoService userInfoService;

    /**
     * 发送验证码
     * @param phone 用户手机号
     * @param session
     * @return 发送验证码成功
     */
    @GetMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable String phone, HttpSession session) {

        // TODO 本应该调用阿里云短信(短信SDK)给phone发送短信，但因为是测试项目，所以使用模拟短信
        String code = "1111";
        // TODO 实际开发中验证码应该是存储到Redis，并且设置时效性; 我们将其存储到session
        session.setAttribute("CODE", code);

        return Result.ok();
    }

    @PostMapping("/register")
    public Result register(@RequestBody RegisterVo registerVo, HttpSession session) {

        // 1. 校验码是否正确
        // 1.1 获取 session 中保存的验证码
        String sessionCode = (String) session.getAttribute("CODE");
        // 1.2 效验
        if (!registerVo.getCode().equalsIgnoreCase(sessionCode)) {
            // 效验失败
            return Result.build(null, ResultCodeEnum.CODE_ERROR);
        }

        // 2. 效验手机号是否存在
        // 2.1 调用业务层的方法根据手机号查询用户信息
        UserInfo userInfo = userInfoService.getByPhone(registerVo.getPhone());
        // 2.2 手机号是否存在
        if (null != userInfo) {
            // 存在
            return Result.build(null,ResultCodeEnum.PHONE_REGISTER_ERROR);
        }

        // 3. 对密码进行加密
        String encryptPassword = MD5.encrypt(registerVo.getPassword());

        // 4. 保存数据
        userInfo = new UserInfo();
        // 将前端传来的属性拷贝进 userInfo
        BeanUtils.copyProperties(registerVo, userInfo);
        // 将加密后的密码放入 userInfo
        userInfo.setPassword(encryptPassword);
        userInfo.setStatus(1);
        userInfoService.insert(userInfo);

        return Result.ok();
    }

    /**
     * 登录
     * @param loginVo 前端传回的登录信息
     * @param session
     * @return 登录成功信息
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo, HttpSession session) {

        UserInfo userInfo = userInfoService.getByPhone(loginVo.getPhone());

        // 判断手机号
        if (null == userInfo) {
            return Result.build(null, ResultCodeEnum.ACCOUNT_ERROR);
        }

        // 判断账号是否被锁定
        if (0 == userInfo.getStatus()) {
            return Result.build(null, ResultCodeEnum.ACCOUNT_LOCK_ERROR);
        }

        // 判断密码是否正确
        if (!userInfo.getPassword().equals(MD5.encrypt(loginVo.getPassword()))) {
            return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
        }
        // 登录成功，将信息存入session
        session.setAttribute("USER", userInfo);

        // 将信息返回给前端
        Map<String, String> responseMapping = new HashMap<>();
        responseMapping.put("nickName", userInfo.getNickName());
        responseMapping.put("phone", userInfo.getPhone());

        return Result.ok(responseMapping);
    }

    /**
     * 登出
     * @param session
     * @return
     */
    @GetMapping("/logout")
    public Result logout(HttpSession session) {
        // 从session中移除当前用户信息，还可以直接销毁session
        session.invalidate();
        return Result.ok();
    }



}
