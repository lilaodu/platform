package com.blockchain.platform.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 提币地址管理VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 12:32 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChequeVO implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 地址
     */
    private String address;

    /**
     * 货币
     */
    private String token;

    /**
     * 备注
     */
    private String remark;

    /**
     * 图标
     */
    private String icon;
}
