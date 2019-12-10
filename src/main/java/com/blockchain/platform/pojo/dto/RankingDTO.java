package com.blockchain.platform.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 排行榜DTO
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-05-18 4:40 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingDTO implements Serializable {

    /**
     * 当前交易货币
     */
    private String symbol;

    /**
     * 交易市场
     */
    private String market;

    /**
     * 货币全称
     */
    private String coinFullname;

    /**
     * 图标
     */
    private String icon;

    /**
     * 小数位数
     */
    private String decLength;

    /**
     * 类型
     */
    private String type;

    /**
     * 是否首页显示
     */
    private String isHome;

    /**
     * 是否新币
     */
    private String isNew;

    /**
     * 排序权重
     */
    private Integer sn;

    /**
     * 交易tick
     */
    private Map<String, Object> tick;
}
