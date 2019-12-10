package com.blockchain.platform.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * OTC广告 分页数据传输DTO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-18 5:59 PM
 **/
@Data
public class OTCAdvertPageDTO implements Serializable {

    /**
     * 支付类型
     */
    private String payType;

    /**
     * 法币(汇率)
     */
    private String rate;



}
