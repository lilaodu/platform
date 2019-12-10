package com.blockchain.platform.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * banner显示VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-16 11:31 AM
 **/
@Data
public class BannerVO implements Serializable {

    /**
     * 图片路径
     */
    private String banner;

    /**
     * 本地路由
     */
    private String routerUrl;

    /**
     * 外部链接地址
     */
    private String linkUrl;

}
