package com.blockchain.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@Entity
@TableName(value = "t_user_assets_flow")
@AllArgsConstructor
@NoArgsConstructor
public class UserAssetsFlowEntity implements Serializable {

    
    @Id
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 类型
     */
    private String type;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private BigDecimal amount;

    /**
     * 货币符号
     */
    private String symbol;

    /**
     * 转的来源
     */
    private String addressFrom;

    /**
     * 目的地址
     */
    private String addressTo;

    /**
     * 转出手续费
     */
    private BigDecimal outFee;

    /**
     * 手续费收取代币
     */
    private String feeSymbol;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 款中：? 申请：? 预审：? 初审：? 复审：? 终审：
     */
    private String state;

    /**
     * 操作发起人
     */
    private Integer originator;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * C2C 金额
     */
    private BigDecimal c2cAmount;

    /**
     * C2C转入时的随机码
     */
    private Integer vCode;

    /**
     * 支付id
     */
    private Integer paymentId;

    /**
     * 冻结金额
     */
    private BigDecimal frozenAmount;

    /**
     * 管理员账号
     */
    private Integer adminUserId;

    /**
     * otc类型
     */
    @Transient
    private String otcType;

}