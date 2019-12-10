package com.blockchain.platform.pojo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.blockchain.platform.annotation.Sharding;

import com.blockchain.platform.i18n.LocaleKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单对象
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderFlowEntity extends Model<Model<?>> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)//指定自增策略
    private Integer id;

    /**
     * 下单用户id
     */
    private Integer userId;

    /**
     * 卖/买单数量
     */
    private BigDecimal num;

    /**
     * 买/卖单价
     */
    private BigDecimal price;

    /**
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 剩余未成交数量
     */
    private BigDecimal surplusNum;

    /**
     * 剩余未成交资金
     */
    private BigDecimal surplusPrice;

    /**
     * 用户手续费
     */
    private BigDecimal fee;

    /**
     * 订单类型 （buy,sell）
     */
    private String type;

    /**
     * 订单取消人id
     */
    private int cancelUserId;

    /**
     * 订单取消时间
     */
    private Date cancelDate;

    /**
     * 订单创建时间
     */
    private Date createDate=new Date();

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 订单状态，如：1.委托2.取消3.完成
     */
    private int state;

    /**
     * 交易对
     */
    private String coinPair;

    /**
     * 币种
     */
    private String symbol;

    /**
     * 需要通知的用户id（不用放入数据库）
     */
    private Integer[] dealIds;

    /**
     * 买家获得代币数量
     */
    private BigDecimal buyCoinNum;

    /**
     * 买家花费基础货币数量
     */
    private BigDecimal buyBaseCoinNum;

    /**
     * 卖家花费代币数量
     */
    private BigDecimal sellCoinNum;

    /**
     * 卖家获得基础货币数量
     */
    private BigDecimal sellBaseCoinNum;

    /**
     * 需要解锁的基础货币数（不用放入数据库）
     */
    private BigDecimal unfreezePrice;

    /**
     * 交易对活动汇率
     */
    private BigDecimal coinFee;

    /**
     * api请求key
     */
    @NotBlank(message = LocaleKey.ORDER_BOOK_KEY_ERROR)


    @TableField(exist = false)
    private String signature;

    /**
     * api请求密钥
     */
    @NotBlank(message = LocaleKey.ORDER_BOOK_SECRET_ERROR)
    @TableField(exist = false)

    private String apiSecret;

    /**
     * 是否执行撮合
     */
    @TableField(exist = false)

    private boolean match = false;

    @TableField(exist = false)
    int limit=0;

    @TableField(exist = false)
     boolean  news=false;
}

