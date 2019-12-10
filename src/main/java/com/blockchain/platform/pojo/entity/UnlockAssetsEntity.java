/**
 * @program: exchange
 * @description: toDo
 * @author: DengWei
 * @create: 2019-05-18
 **/
package com.blockchain.platform.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *解仓实体类
 **/
@Data
@Entity
@TableName("t_unlock_assets")
public class UnlockAssetsEntity extends Model<Model<?>> implements Serializable {
	
	@Id
	@TableId(value = "id",type = IdType.AUTO)//指定自增策略
    private Integer id;

    private Integer userId;

    //货币简称
    private String symbol;
    
    private BigDecimal forzenAmount;

    private BigDecimal amount;

    private BigDecimal unlockPerDay;

    private String perfix;

    private String txFlowId;


}
