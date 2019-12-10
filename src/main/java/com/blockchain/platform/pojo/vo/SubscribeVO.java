package com.blockchain.platform.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 订阅返回对象
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-08-30 2:22 PM
 **/
@Data
public class SubscribeVO <T> implements Serializable {


    /**
     * 订阅类型
     */
    private String topic;

    /**
     * 订阅交易对
     */
    private String symbol;

    /**
     * 返回数据
     */
    private T data;
}
