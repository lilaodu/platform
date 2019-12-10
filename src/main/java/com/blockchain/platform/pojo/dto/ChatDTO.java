package com.blockchain.platform.pojo.dto;

import com.blockchain.platform.constant.BizConst;
import lombok.Data;

import java.io.Serializable;

/**
 * Chat关注对象
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-08-30 5:00 PM
 **/
@Data
public class ChatDTO implements Serializable{

    /**
     * 类型
     */
    private String topic;

    /**
     * 用户token
     */
    private String token;

    /**
     * 发送内容
     */
    private String content;

    /**
     * 广告ID
     */
    private String advertNo;

    /**
     * 订单ID
     */
    private String orderNo;

    /**
     * 用户头像
     */
    private String headImage;

}
