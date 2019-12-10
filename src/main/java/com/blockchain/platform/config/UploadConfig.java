package com.blockchain.platform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件上传配置
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-05-20 11:38 AM
 **/
@Data
@Component
@ConfigurationProperties(prefix = "platform.upload")
public class UploadConfig {

    /**
     * 访问路径
     */
    private String domain;

    /**
     * 请求路径
     */
    private String action;

}
