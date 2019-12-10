/**
 * @program: exchange
 * @description: toDo
 * @author: DengWei
 * @create: 2019-06-02
 **/
package com.blockchain.platform.pojo.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**挖矿VO
 * @author: zjl
 **/
@Builder
public @Data class DigVO {

    //当日将解锁资金
    private BigDecimal unlockAmount;

    //当日总共可解锁资金
    private BigDecimal totalAmount;

    //单给解锁笔数
    private int unlockNum;

    //当日总共解锁笔数
    private int totalNum;

    //总奖励资金
    private BigDecimal bonusAmount;

    /**
     * 冻结
     */
    private BigDecimal frozenNum;
}
