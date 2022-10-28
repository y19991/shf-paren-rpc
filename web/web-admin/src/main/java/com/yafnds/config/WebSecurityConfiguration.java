package com.yafnds.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * 类描述：
 * <p>创建时间：2022/10/19/019 11:32
 *
 * @author yafnds
 * @version 1.0
 */
@Configuration
@EnableWebSecurity // 开启 SpringSecurity 的默认行为
@EnableGlobalMethodSecurity(prePostEnabled = true)  // 开启方法注解权限配置
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    // 注入加密器
    @Autowired
    private PasswordEncoder passwordEncoder;
    // 注入拒绝访问处理器
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    /**
     * 安全过滤器链配置
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 允许页面嵌套
        http.headers().frameOptions().disable();

        http.authorizeRequests()
                // 哪些请求不需要认证就可以访问
                .antMatchers("/static/**", "/login").permitAll()
                // 哪些请求需求认证才能访问
                .anyRequest().authenticated()

                .and()

                .formLogin()
                // 指定哪个资源作为登录页面
                .loginPage("/login")
                // 指定登录成功之后跳转到哪个资源
                .defaultSuccessUrl("/")

                .and()

                .logout()
                // 退出登录的路径，指定 SpringSecurity 拦截的注销url，退出功能是 Security 提供的
                .logoutUrl("/logout")
                // 用户退出后要被重定向的url
                .logoutSuccessUrl("/logout");

        // 关闭跨域请求伪造
        http.csrf().disable();

        //当权限不够时直接显示异常页面
        //http.exceptionHandling().accessDeniedPage("/auth");

        // 当权限不够时,可以先通过访问拒绝处理器进行一些你想要的处理
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

    /**
     * 指定加密方式
     * @return
     */
    @Bean
    public PasswordEncoder createPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
