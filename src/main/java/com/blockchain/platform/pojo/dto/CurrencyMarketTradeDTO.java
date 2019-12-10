package com.blockchain.platform.pojo.dto;

import java.math.BigDecimal;

import com.blockchain.platform.pojo.dto.OrderDTO.OrderDTOBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对应货币可取到的最近一次的交易价格信息
 * @author zhangye
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyMarketTradeDTO {
	
	/**
	 * 唯一交易id
	 */
	private Integer id;
	
	/**
	 * 以基础币种为单位的交易量
	 */
	private BigDecimal amount;
	
	/**
	 * 以报价币种为单位的成交价格
	 */
	private BigDecimal price;
	
	/**
	 * 调整为北京时间的时间戳，单位毫秒
	 */
	private Integer ts;
	
	/**
	 * 交易方向：“buy” 或 “sell”, “buy” 即买，“sell” 即卖
	 */
	private String direction;
	
	
}
