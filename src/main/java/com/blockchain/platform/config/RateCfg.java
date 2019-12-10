package com.blockchain.platform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 汇率配置
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-14 11:41 AM
 **/
@Data
@Component
@ConfigurationProperties(prefix = "platform.rate")
public class RateCfg {

    /**
     * 实际配置信息
     */
    private Map<String, Object> configure;


    /**
     * kucoin接口API
     */
    private String kucoinApi;

    /**
     * coinbase 接口API
     */
    private String coinbaseApi;


    /**
     * 市场
     */
    private Map<String, String> market;
}
