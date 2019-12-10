package com.blockchain.platform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 短信发送配置信息
 *
 * @author ml
 * @version 1.0
 * @create 2019-04-29 8:47 PM
 **/
@Data
@Component
@ConfigurationProperties(prefix = "platform.email")
public class EmailConfig {

    /**
     * 发送邮箱
     */
    private String url;

    /**
     * 用户名
     */
    private String appId;

    /**
     * 密码
     */
    private String appKey;

    /**
     * 发出地址
     */
    private String from;
}
