package com.blockchain.platform.pojo.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * tv symbol vo
 **/
@Data
@Builder
public class TvConfigVO implements Serializable {

    /**
     * 交易对
     */
    private String ticker;

    /**
     * 描述
     */
    private String description;

    /**
     * 名称
     */
    private String name;

    /**
     * 所处市场
     */
    private String currency_code;

    /**
     * 时区
     */
    private String timezone;

    /**
     * session 时效
     */
    private String session;

    /**
     * 盘内数据
     */
    private Boolean has_intraday;

    /**
     * 盘内时间切换
     */
    private String [] supported_resolutions;

    /**
     * 是否包含深度
     */
    private Boolean has_no_volume;

    /**
     * 类型
     */
    private String type;

    /**
     * 盘内值
     */
    private Integer minmov2;

    private Integer minmov;

    private Integer pointvalue;

    /**
     * 小数位数
     */
    private Integer pricescale;

    /**
     * 深度位数
     */
    private Integer volume_precision;
}
