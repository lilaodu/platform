package com.blockchain.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 抽奖实体对象
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-13 5:46 PM
 **/
@Data
@Entity
@TableName(value = "t_luck_draw")
public class LuckDrawEntity implements Serializable {

    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 抽奖名称
     */
    private String name;

    /**
     * 图片
     */
    private String image;

    /**
     * 小图标
     */
    private String icon;

    /**
     * 是否首页展示
     */
    private String isHome;

    /**
     * 排序字段
     */
    private Integer sn;

    /**
     * 每日奖励总数量
     */
    private BigDecimal dayAmount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 编辑时间
     */
    private Date updateTime;

    /**
     * kyc 认证人数换一次抽奖机会
     */
    private  Integer kycNum;

    /**
     * 秒合约订单数，换一次抽奖机会
     */
    private Integer contractNum;
}
