package com.blockchain.platform.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-08-27 2:16 PM
 **/
@Configuration
public class InterceptorConfigurer implements WebMvcConfigurer {

    /**
     * 登录拦截器
     * @return
     */
    @Bean
    public LoginInterceptor getLoginInterceptor(){
        return new LoginInterceptor();
    }


    /**
     * 授权拦截器
     * @return
     */
    @Bean
    public CipherInterceptor getCipherInterceptor(){
        return new CipherInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录
        registry.addInterceptor( getLoginInterceptor())
        		.addPathPatterns("/user/*", "/order/*","/wallet/*","/team/*","/otc/chat/*",
                        "/secondsContract/activeings","/secondsContract/stake","/secondsContract/activeings")
                .excludePathPatterns("/user/register", "/user/login", "/user/favor" ,
                        "/user/geetest","/user/forget","/order/tradeMatch","/quotation/optional","/order/robotBook");

        // 授权
        registry.addInterceptor( getCipherInterceptor())
                .addPathPatterns("/order/*","/secondsContract/stake")
                .excludePathPatterns("/order/tradeMatch","/order/actives","/order/robotBook");
    }
}
