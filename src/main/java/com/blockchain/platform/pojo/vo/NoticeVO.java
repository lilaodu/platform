package com.blockchain.platform.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 通知消息VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-16 12:24 AM
 **/
@Data
public class NoticeVO implements Serializable {

    /**
     * id
     */
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 图片URL
     */
    private String imageUrl;

    /**
     * 连接地址
     */
    private String linkUrl;

    /**
     * 是否首页显示
     */
    private String isHome;

    /**
     * 是否单独显示
     */
    private String single;

}
