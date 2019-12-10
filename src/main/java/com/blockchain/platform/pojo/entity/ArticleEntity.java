package com.blockchain.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 文章数据
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-16 1:47 PM
 **/
@Data
@Entity
@TableName("t_article")
public class ArticleEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 标题
     */
    @Column(name = "caption")
    private String caption;

    /**
     * 内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 分类
     */
    @Column(name = "category")
    private String category;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 编辑人
     */
    @Column(name = "editor")
    private Integer editor;

    /**
     * 发布时间
     */
    @Column(name = "pub_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pubTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 版本号
     */
    @Column(name = "version")
    private Integer version;

    /**
     * 状态
     */
    @Column(name = "state")
    private Integer state;

}
