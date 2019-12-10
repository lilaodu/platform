package com.blockchain.platform.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class KLinesVo {
    public KLinesVo(){

    }
    public KLinesVo(String period){
        this.period = period;
    }
    private BigDecimal openPrice = BigDecimal.ZERO;
    private BigDecimal highestPrice  = BigDecimal.ZERO;
    private BigDecimal lowestPrice  = BigDecimal.ZERO;
    private BigDecimal closePrice  = BigDecimal.ZERO;
    private String symmbol;
    private long time;
    private String period;
    public KLinesVo(String period, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close, long time){

        this.period=period;
        this.openPrice=open;
        this.highestPrice=high;
        this.lowestPrice=low;
        this.closePrice=close;

        this.time=time;
    }
    /**
     * 成交笔数
     */
    private int count;
    /**
     * 成交量
     */
    private BigDecimal volume = BigDecimal.ZERO;
    /**
     * 成交额
     */
    private BigDecimal turnover = BigDecimal.ZERO;
}
