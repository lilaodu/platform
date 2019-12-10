package com.blockchain.platform.pojo.entity;

import lombok.Data;
import javax.persistence.*;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 货币配置
 *@author zhangye
 **/
@Data
@Entity
@TableName("t_coin_config")
public class CoinEntity extends Model<Model<?>> implements Serializable {

    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 货币简称
     */
    @Column(name = "symbol")
    private String symbol;

    /**
     * 货币图标地址
     */
    @Column(name = "icon")
    private String icon;

    /**
     * 是否是基础货币
     */
    @Column(name = "is_basics")
    private String isBasics;

    /**
     * 是否首页展示
     */
    @Column(name = "is_home")
    private String isHome;

    /**
     * 货币全称
     */
    @Column(name = "coin_fullname")
    private String coinFullname;

    /**
     * 排序
     */
    @Column(name = "sn")
    private Integer sn;

    /**
     *
     */
    @Column(name = "out_fee")
    private BigDecimal outFee;

    /**
     * 记录时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 资产转入下限
     */
    @Column(name = "lower_limit_in")
    private BigDecimal lowerLimitIn;

    /**
     * 用户提现下限
     */
    @Column(name = "lower_limit_out")
    private BigDecimal lowerLimitOut;

    /**
     * 用户提现，高于或等于此值时，需要人工审核成功，才能发放
     */
    @Column(name = "audit")
    private BigDecimal audit;

    /**
     *
     */
    @Column(name = "dec_length")
    private String decLength;

    /**
     *
     */
    @Column(name = "min_buy")
    private BigDecimal minBuy;

    /**
     *
     */
    @Column(name = "max_buy")
    private BigDecimal maxBuy;

    /**
     *
     */
    @Column(name = "allow_in")
    private String allowIn;

    /**
     *
     */
    @Column(name = "allow_out")
    private String allowOut;

    /**
     *
     */
    @Column(name = "confirm_sum")
    private String confirmSum;

    /**
     * 前缀
     */
    @Column(name = "prefix")
    private String prefix;

    /**
     * 24小时统计
     */
    @Column(name = "amount24")
    private Integer amount24;

    /**
     * kyc 统计
     */
    @Column(name = "amount_kyc")
    private Integer amountKyc;

    /**
     * com 统计
     */
    @Column(name = "amount_com")
    private Integer amountCom;

    /**
     * 货币费率
     */
    @Column(name = "fee_symbol")
    private String feeSymbol;

    /**
     * 类型
     */
    @Column(name = "type")
    private String type;

    /**
     * 状态
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 版本号
     */
    @Column(name = "version")
    private Integer version;

    /**
     *
     */
    @Column(name = "unlock_perday")
    private Integer unlockPerday;

    /**
     * 冻结
     */
    @Column(name = "freeze")
    private String freeze;

    /**
     * 循环
     */
    @Column(name = "circulation")
    private String circulation;

    /**
     * 价格
     */
    @Column(name = "price")
    private BigDecimal price;

    /**
     * 网址
     */
    @Column(name = "website")
    private String website;

    /**
     *
     */
    @Column(name = "white_paper")
    private String whitePaper;

    /**
     * 解释
     */
    @Column(name = "explains")
    private String explains;

    /**
     * 发行时间
     */
    @Column(name = "issue_time")
    private Date issueTime;

    /**
     * 发行量
     */
    @Column(name = "issue")
    private String issue;

    /**
     * 区块链查询地址
     */
    @Column(name = "block_url")
    private String blockUrl;

    /**
     * 中文名称
     */
    @Column(name = "chinese_name")
    private String chineseName;

    /**
     * 是否新币
     */
    @Column(name = "is_new")
    private String isNew;

    /**
     * 是否支持法币账户
     */
    @Column(name = "is_otc")
    private String isOtc;

    /**
     * 是否支持合约账户
     */
    @Column(name = "is_t")
    private String isT;

    /**
     * 大类
     */
    @Column(name = "coin_type")
    private String coinType;

    /**
     * 钱包地址
     */
    @TableField(exist=false)
    private String walletAddress;

    /**
     * 是否支持多协议
     */
    private Integer isManyProtocol;

    /**
     * 多协议类型
     */
    private String manyProtocolType;

    /**
     * 折算价格 obversion
     */
    private BigDecimal marketPrice;
    
}