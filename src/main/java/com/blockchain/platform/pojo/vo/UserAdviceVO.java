package com.blockchain.platform.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 咨询建议VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-06-28 9:24 PM
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAdviceVO implements Serializable {

    /**
     * 咨询内容编号
     */
    private String billNo;

    /**
     * 咨询时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date time;

    /**
     * 是否为回复 0 不是  1 是
     */
    private Integer state;

    /**
     * 用户名
     */
    private String mobile;

    /**
     * 后台展示用
     */
    private Integer id;

    /**
     * 内容
     */
    private List<ContentVO> details;



}
