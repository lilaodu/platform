package com.blockchain.platform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 交易所短信配置
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-04-29 8:47 PM
 **/
@Data
@Component
@ConfigurationProperties(prefix = "platform.sms")
public class SMSConfig {

    /**
     * 模板标识
     */
    private String sign;

    /**
     * 短信发送站
     */
    private String domain;

    /**
     * 用户id
     */
    private String appId;

    /**
     * 短信执行方法
     */
    private String appKey;

    /**
     * 提币地址内容
     */
    private String addressContent;

    /**
     * 提币地址
     */
    private Map<String, Object> addressParam;

    /**
     * 转出
     */
    private String extractContent;

    /**
     * 转出
     */
    private Map<String, Object> extractParam;

    /**
     * 短信内容
     */
    private String registerContent;

    /**
     * 短信参数
     */
    private Map<String, Object> registerParam;


    /**
     * 资金模板
     */
    private String cipherContent;

    /**
     * 资金数据
     */
    private Map<String, Object> cipherParam;

    /**
     * 绑定邮箱，电话短信模板
     */
    private String bindContent;

    /**
     * 绑定邮箱，电话参数
     */
    private Map<String, Object> bindParam;

    /**
     * 提币邮箱，电话短信模板
     */
    private String withdrawContent;

    /**
     * 提币邮箱，电话参数
     */
    private Map<String, Object> withdrawParam;
    String changeCipher;

}
