package com.blockchain.platform.pojo.vo;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 代币充币记录展示VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-22 3:38 PM
 **/
@Data
public class DepositVO implements Serializable {

    /**
     * 货币大类
     */
    private String coinCode;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 转入地址
     */
    private String toAddress;

    /**
     * 数据状态
     */
    private String status;

    /**
     * 确认数
     */
    private Integer confirmNum;

    /**
     * 创建时间
     */
    private long time;

    /**
     * 格式化时间
     * @return
     */


    String hash;
}
