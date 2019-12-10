package com.blockchain.platform.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 查询tick
 * @author zhangye
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TickDTO implements Serializable {

    /**
     * 当前时间
     */
    private Long now;

    /**
     * 当前交易对
     */
    private String symbol;

    /**
     * 当日时间
     */
    private Long ts;
}
