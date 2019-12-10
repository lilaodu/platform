package com.blockchain.platform.pojo.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 抽奖活动展示VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-15 2:10 PM
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LuckDrawConfigVO implements Serializable {

    /**
     * id
     */
    private Integer id;

    /**
     * 文字
     */
    private String words;

    /**
     * 图片
     */
    private String image;

    /**
     * 代币
     */
    private String symbol;

    /**
     * 概率ss
     */
    private Double probability;

    /**
     * 奖项（几等奖）
     */
    private  Integer prize;

    /**
     *  数量
     */
    private BigDecimal num;

    /**
     * 类型(奖项还是按钮)
     */
    private String type;

    /**
     * 位置
     */
    private Integer position;
}
