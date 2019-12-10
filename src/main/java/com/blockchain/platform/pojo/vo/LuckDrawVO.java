package com.blockchain.platform.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 抽奖活动展示VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-15 2:10 PM
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LuckDrawVO implements Serializable {

    /**
     * id
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 图片
     */
    private String image;

    /**
     * 图标
     */
    private String icon;

    /**
     * 是否首页展示
     */
    private String isHome;

    /**
     * 单日奖励数量
     */
    private BigDecimal dayAmount;

    /**
     * kyc认证人数换一次抽奖机会
     */
    private Integer kycNum;

    /**
     * 秒合约订单数换一次抽奖机会
     */
    private Integer contractNum;

    /**
     * 所有的奖项列表
     */
    List<LuckDrawConfigVO> prize;
}
