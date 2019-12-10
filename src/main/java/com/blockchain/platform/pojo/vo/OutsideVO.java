/**
 * @program: exchange
 * @description: toDo
 * @author: DengWei
 * @create: 2019-05-29
 **/
package com.blockchain.platform.pojo.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: OutsideVO
 * @description: 场外交易视图对象
 * @author: DengWei
 * @create: 2019-05-29 11:30
 **/
@Data
@Builder
public class OutsideVO {

    //场外交易购买价格
    private String buyPrice;

    //场外交易出售价格
    private String sellPrice;

    //场外交易购买价格
    private String buyTime;

    //场外交易出售价格
    private String sellTime;

    //场外交易最低购买
    private BigDecimal limitIn;

    //场外交易最少出售
    private BigDecimal limitOut;

    //用户资金
    private BigDecimal amount;

}
