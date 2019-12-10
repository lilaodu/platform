package com.blockchain.platform.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 抽奖WSDTO
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-05 1:55 PM
 **/
@Data
public class DrawWsDTO implements Serializable {

    /**
     * 抽奖id
     */
    private Integer drawId;

    /**
     * 用户token
     */
    private String token;

    /**
     * 关注数据
     */
    private String topic;
}
