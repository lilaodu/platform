package com.blockchain.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.blockchain.platform.utils.BigDecimalUtils;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@TableName("t_deal_flow")
public class DealFlowEntity implements Serializable {

    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 卖单主键id
     */
    private Integer sellId;

    /**
     * 买单主键id
     */
    private Integer buyId;

    /**
     * 卖单人id
     */
    private Integer sellUserId;

    /**
     * 买单人id
     */
    private  Integer buyUserId;

    /**
     * 成交时间
     */
    private Date dealTime;

    /**
     * 成交数
     */
    private BigDecimal dealNum;

    /**
     * 成交单价
     */
    private BigDecimal  dealPrice;

    /**
     * 成交总价
     */
    private BigDecimal totalPrice;

    /**
     * 主动撮合订单类型
     */
    private String type;

    /**
     * 买单手续费
     */
    private BigDecimal buyCharge;

    /**
     * 买单汇率
     */
    private BigDecimal buyFee;

    /**
     * 卖单手续费
     */
    private BigDecimal sellCharge;

    /**
     * 卖单汇率
     */
    private BigDecimal sellFee;
}
