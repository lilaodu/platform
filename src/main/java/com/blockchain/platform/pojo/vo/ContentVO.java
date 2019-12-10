package com.blockchain.platform.pojo.vo;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 咨询内容VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 1:24 PM
 **/
@Data
public class ContentVO implements Serializable{

    /**
     * 是否为 回复 0 false 1 true
     */
    private Integer isReply;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 回复类型
     */
    private String type;

    /**
     * 回复时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date time;
}
