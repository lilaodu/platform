package com.blockchain.platform.pojo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.pojo.dto.UserDTO.UserDTOBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLockWarehouseDTO implements Serializable {
	
	/**
	 * 用户锁的币的数量
	 */
	@Min(value = 0, message = LocaleKey.OTC_ORDER_AMOUNT_LESS_ZERO)
	private BigDecimal lockNum;
	
	
	/**
	 * 币种(btb)
	 */
	@NotEmpty(message = LocaleKey.SYS_PARAM_ERROR)
	private String coin;

}
