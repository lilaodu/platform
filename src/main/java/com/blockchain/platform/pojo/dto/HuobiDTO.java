package com.blockchain.platform.pojo.dto;

import com.blockchain.platform.i18n.LocaleKey;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 抽奖数据
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-05 1:26 PM
 **/
@Data
public class HuobiDTO implements Serializable {

    /**
     * 开始时间戳
     */
    private Long from;

    /**
     * 结束时间戳
     */
    private Long to;

    /**
     * 数据条数
     */
    private Integer size;

    /**
     * 交易对
     */
    @NotEmpty(message = LocaleKey.SYS_PARAM_ERROR)
    private String symbol;

    /**
     * 周期 (15min)
     */
    @NotEmpty(message = LocaleKey.SYS_PARAM_ERROR)
    private String period;

}
