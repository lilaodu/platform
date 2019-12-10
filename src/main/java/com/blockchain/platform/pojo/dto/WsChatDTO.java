package com.blockchain.platform.pojo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * otc聊天数据传输类
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-23 6:10 PM
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WsChatDTO implements Serializable {

    /**
     * 广告id
     */
    private Integer advertId;

    /**
     * token
     */
    private String token;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 消息内容
     */
    private String content;
}
