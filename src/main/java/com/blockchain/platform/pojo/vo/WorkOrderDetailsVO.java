package com.blockchain.platform.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 工单详情展示VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-26 2:37 PM
 **/
@Data
public class WorkOrderDetailsVO implements Serializable {

    /**
     * 内容
     */
    private String content;

    /**
     * 是否是自己的消息
     */
    private String own;
}
