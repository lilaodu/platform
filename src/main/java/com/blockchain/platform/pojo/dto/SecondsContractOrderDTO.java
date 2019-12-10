package com.blockchain.platform.pojo.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import com.blockchain.platform.i18n.LocaleKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecondsContractOrderDTO implements Serializable {
	
    /**
     * 配置id
     */
	@NotEmpty(message = LocaleKey.SYS_PARAM_ERROR)
    private Integer secondsContractId;
    
    /**
     * 1.买涨
     * 2.买跌
     */
	@NotEmpty(message = LocaleKey.SYS_PARAM_ERROR)
    private Integer type;
    
    /**
     * 押注金额
     */
	@Min(value = 0, message = LocaleKey.OTC_ORDER_AMOUNT_LESS_ZERO)
    private BigDecimal price;
	
	/**
     * 押注交易对
     */
	@NotEmpty(message = LocaleKey.SYS_PARAM_ERROR)
    private String coinPair;
    
    /**
     * 付费币种
     */
	@NotEmpty(message = LocaleKey.SYS_PARAM_ERROR)
    private String payCoin;
	
	/**
	 * 时间区间
	 */
	@NotEmpty(message = LocaleKey.SYS_PARAM_ERROR)
	 private String section;

	private int  state;
	
	
}
