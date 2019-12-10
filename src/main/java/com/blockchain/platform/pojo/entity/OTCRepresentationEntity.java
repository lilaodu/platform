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
 * otc广告实体对象
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-18 11:48 AM
 **/
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_otc_representation")
public class OTCRepresentationEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 申述类型
     */
    private String type;

    /**
     * 申述理由
     */
    private String reason;

    /**
     * 证明材料
     */
    private String material;

    /**
     * 申述状态
     */
    private Integer state;

    /**
     * 申述人
     */
    private Integer userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 审核时间
     */
    private Date auditTime;
}
