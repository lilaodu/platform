package com.blockchain.platform.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class KLineInsertMessageVo {
    String pair;
    BigDecimal price;
    BigDecimal number;
    long timestamp;
    public KLineInsertMessageVo(String pair,
                                BigDecimal price,
                                BigDecimal number,
                                long timestamp){
        this.pair = pair;
        this.price = price;
        this.number = number;
        this.timestamp = timestamp;
    }
}
