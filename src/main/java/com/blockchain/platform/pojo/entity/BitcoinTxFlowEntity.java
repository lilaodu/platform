package com.blockchain.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 比特币转出流水
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-24 4:11 PM
 **/
@Data
@Entity
@TableName(value = "t_bitcoin_tx_flow")
public class BitcoinTxFlowEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 货币大类
     */
    private String coinType;

    /**
     * 代币符号
     */
    private String symbol;

    /**
     * 交易标志
     */
    private String txHash;

    /**
     * 区块高度
     */
    private Long blockHeight;

    /**
     * 交易查看链接
     */
    private String blockUrl;

    /**
     * 临时凭证
     */
    private String nonce;

    /**
     * 转出人
     */
    private Integer userId;

    /**
     * 随机数
     */
    private String random;

    /**
     * 转出金额
     */
    private BigDecimal amount;

    /**
     *
     */
    private Integer sign;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 数据状态
     */
    private Integer state;

    /**
     * 转出地址
     */
    private String addressFrom;

    /**
     * 转入地址
     */
    private String addressTo;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 版本号
     */
    private Integer version;
}
