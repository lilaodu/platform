package com.blockchain.platform.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * TV DTO
 * @author zhangye
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TvDTO implements Serializable {

    /**
     * 当前交易对
     */
    private String symbol;

    /**
     * 开始时间
     */
    private Long from;

    /**
     * 截止时间
     */
    private Long to;

    /**
     * 断
     */
    private String resolution;


    private Integer limit;//最大获取条数,最大为2000条
}
