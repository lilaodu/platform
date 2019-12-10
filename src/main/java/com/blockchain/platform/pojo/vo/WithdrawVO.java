package com.blockchain.platform.pojo.vo;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 代币提币记录展示VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-22 3:38 PM
 **/
@Data
public class WithdrawVO implements Serializable {

    /**
     * 货币大类
     */
    private String coinCode;

    /**
     * 地址
     */
    private String address;

    /**
     * 数量
     */
    private BigDecimal number;

    /**
     * 费率
     */
    private BigDecimal fee;

    /**
     * 数据状态
     */
    private String status;

    /**
     * 费率代币
     */
    private String feeCoinCode;

    /**
     * 时间
     */
    private long time;

    /**
     * hash值
     */
    private String hash;


    @Column(name = "real_fee")
    BigDecimal realFee;


}
