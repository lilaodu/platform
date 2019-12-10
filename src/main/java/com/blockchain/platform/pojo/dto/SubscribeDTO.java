package com.blockchain.platform.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 订阅消息
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-08-28 8:41 PM
 **/
@Data
public class SubscribeDTO implements Serializable {


    /**
     * 用户token
     */
    private String token;

    /**
     * 交易对
     */
    private String symbol;

    /**
     * 订阅类型
     */
    private String type;


    private Long from;


    private Long to;
    
}
