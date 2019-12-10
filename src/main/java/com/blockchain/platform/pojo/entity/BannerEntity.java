package com.blockchain.platform.pojo.entity;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * banner实体对象
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-16 11:37 AM
 **/
@Data
@Entity
@TableName("t_banner")
public class BannerEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * banner路径
     */
    @Column(name = "banner")
    private String banner;

    /**
     * 语言标志
     */
    @Column(name = "lang")
    private String lang;

    /**
     * 开始时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 排序
     */
    @Column(name = "sn")
    private Integer sn;

    /**
     * 外部链接地址
     */
    @Column(name = "link_url")
    private String linkUrl;

    /**
     * 路由
     */
    @Column(name = "router_url")
    private String routerUrl;

    /**
     * 参数
     */
    @Column(name = "params")
    private String params;

    /**
     * 状态（0：无效，1：有效）
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 版本号
     */
    @Column(name = "version")
    private Integer version;
}
