package com.blockchain.platform.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 消息发送DTO
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-05-19 10:17 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsDTO implements Serializable {

    /**
     * 密码
     */
    private String appid;

    /**
     * 账号
     */
    private String to;

    /**
     * 用户id
     */
    private Integer project;

    /**
     * 接口方法
     */
    private String signature;

    /**
     * 短信内容
     */
    private String content;
}
