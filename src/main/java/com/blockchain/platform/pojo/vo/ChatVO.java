package com.blockchain.platform.pojo.vo;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 消息VO
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-08-30 6:29 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatVO <T> implements Serializable {

    /**
     * 订单ID
     */
    private String orderNo;


    /**
     * 广告ID
     */
    private String advertNo;

    /**
     * 类型
     */
    private String type;

    /**
     * 数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long ts = DateUtil.currentSeconds();
}
