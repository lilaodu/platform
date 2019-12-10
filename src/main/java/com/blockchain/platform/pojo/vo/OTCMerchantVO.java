package com.blockchain.platform.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * OTC商户显示VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-18 5:34 PM
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY )
public class OTCMerchantVO implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 头像地址
     */
    private String headImage;

    /**
     * 商户昵称
     */
    private String nickName;

    /**
     * 状态
     */
    private Integer state;
}
