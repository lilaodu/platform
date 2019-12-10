package com.blockchain.platform.config;

import lombok.Data;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * SLB配置
 **/
@Data
@Component
@ConfigurationProperties(prefix = "platform.slb")
public class SlbConfig {

    /**
     * SLB均衡地址
     */
    private String ipConfig;

    /**
     * 通知方法
     */
    private String action;
    
    /**
     * 秒合约通知方法
     */
    private String scaction;

    /**
     * 项目路径
     */
    private String contextPath;

    /**
     * 断开
     */
    private String port;
    
    /**
     * 撮合分发配置
     **/
    private Map<String,String> match;
    
    
}
