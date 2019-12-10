package com.blockchain.platform.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章显示VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-16 4:54 PM
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY )
public class ArticleVO implements Serializable {

    /**
     * 主键
     */
    private Integer id;


    /**
     * 发布时间
     * 使用
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date pubTime;

    /**
     * 文章标题
     */
    private String caption;

    /**
     * 文章分类
     */
    private String category;

    /**
     * 文章内容
     */
    private String content;
}
