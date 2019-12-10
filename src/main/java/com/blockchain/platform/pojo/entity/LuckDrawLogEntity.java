package com.blockchain.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 抽奖记录实体对象
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-13 5:59 PM
 **/
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_luck_draw")
public class LuckDrawLogEntity implements Serializable {

    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户账号（邮箱或手机）
     */
    private String username;

    /**
     * 抽奖活动id
     */
    private Integer drawId;

    /**
     * 抽奖奖项id
     */
    private Integer configId;

    /**
     * 是否中奖
     */
    private String isPrize;

    /**
     * 货币符号
     */
    private String symbol;

    /**
     * 中奖数量
     */
    private BigDecimal num;

    /**
     * 创建时间
     */
    private  Date createTime;
}
