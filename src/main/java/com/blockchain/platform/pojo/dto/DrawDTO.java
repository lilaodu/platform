package com.blockchain.platform.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 抽奖数据
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-05 1:26 PM
 **/
@Data
public class DrawDTO implements Serializable {

    /**
     * 抽奖活动id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户账号
     */
    private String name;
}
