package com.blockchain.platform.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 极验返回值
 *
 * @author zjl
 **/
@Data
public class GeetestVO implements Serializable {

    /**
     * hash值
     */
    private String txHash;

    /**
     * gt 初始状态
     */
    private Integer success;

    private String gt;

    private String challenge;

    /**
     * 是否新 gt
     */
    private Boolean newFailback;

    /**
     * gt服务状态
     */
    private Integer gt_server_status;
}
