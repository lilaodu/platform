package com.blockchain.platform.pojo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * otc信任管理实体对象
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-18 11:40 AM
 **/
@Data
@Entity
@TableName("t_otc_trust")
public class OTCTrustEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 广告id
     */
    private Integer advertId;

    /**
     * 被屏蔽/信任/举报的用户
     */
    private Integer passiveUserId;

    /**
     * 类型（1：屏蔽，2：信任,3：举报）
     */
    private String type;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 举报原因
     */
    private String reason;

    /**
     * 举报内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 编辑时间
     */
    private Date updateTime;
}
