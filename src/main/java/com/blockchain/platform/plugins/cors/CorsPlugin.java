package com.blockchain.platform.plugins.cors;

import cn.hutool.core.util.StrUtil;
import com.blockchain.platform.constant.AppConst;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 后端跨域设置
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-15 1:54 PM
 **/
@Configuration
public class CorsPlugin {

    @Bean
    public CorsFilter corsFilter() {
        //1.添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        //放行哪些原始域
        config.addAllowedOrigin("*");
        //是否发送Cookie信息
        config.setAllowCredentials(true);
        //放行哪些原始域(请求方式)
        config.addAllowedMethod("*");
        //放行哪些原始域(头部信息)
        config.addAllowedHeader("*");
        //
        config.setMaxAge( 6000L);
        //暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
        config.addExposedHeader( AppConst.TOKEN);
        config.addExposedHeader( AppConst.LANG);

        //2.添加映射路径
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);

        //3.返回新的CorsFilter.
        return new CorsFilter(configSource);
    }
}
