package com.blockchain.platform.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 签名VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-30 4:37 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignVO implements Serializable {

    /**
     * 数据签名
     */
    private String signature;

    /**
     * 签名时间错
     */
    private Long timestamp;
}
