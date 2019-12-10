package com.blockchain.platform.pojo.entity;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体对象
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-16 11:37 AM
 **/
@Data
@Entity
@TableName("t_activity")
public class ActivityEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 活动标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 活动图片url
     */
    @Column(name = "image_url")
    private String imageUrl;

    /**
     * 活动内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 语言标志
     */
    @Column(name = "lang")
    private String lang;

    /**
     * 排序
     */
    @Column(name = "sn")
    private Integer sn;

    /**
     * 是否福利
     */
    @Column(name = "is_welfare")
    private String isWelfare;

    /**
     * 记录时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private Date endTime;

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

    /**
     * 外部链接地址
     */
    @Column(name = "link_url")
    private String linkUrl;

    /**
     * 是否首页显示
     */
    @Column(name = "is_home")
    private String isHome;

    /**
     * 是否单独显示
     */
    @Column(name = "single")
    private String single;

    /**
     * 修改人或上传人id
     */
    @Column(name = "user_id")
    private Integer userId;
}
