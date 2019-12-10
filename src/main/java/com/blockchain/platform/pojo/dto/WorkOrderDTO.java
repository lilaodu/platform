package com.blockchain.platform.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 工单回复数据传输DTO
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-26 2:37 PM
 **/
@Data
public class WorkOrderDTO implements Serializable {

    /**
     * 问题
     */
    private String question;

    /**
     * 原因
     */
    private String reason;

    /**
     * 证明材料
     */
    private String material;

    /**
     * 工单id
     */
    private Integer workId;

    /**
     * 内容
     */
    private String content;
}
