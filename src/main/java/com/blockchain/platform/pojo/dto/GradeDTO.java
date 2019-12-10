package com.blockchain.platform.pojo.dto;

import com.blockchain.platform.pojo.vo.RecommendVO;
import com.blockchain.platform.utils.BigDecimalUtils;
import com.blockchain.platform.utils.IntUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 用户 -- 等级详情VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-26 6:39 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY )
public class GradeDTO implements Serializable {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 直推人数
     */
    private Integer num = IntUtils.INT_ZERO;

    /**
     * 用户等级
     */
    private String rank;

    /**
     * 个人合约账户余额
     */
    private BigDecimal asset;

    /**
     * AC（军长）等级人数
     */
    private Integer ACNum;

    /**
     * DC（师长）等级人数
     */
    private Integer DCNum;

    /**
     * RC（团长）等级人数
     */
    private Integer RCNum;

    /**
     * 直推人员个人合约账户 >= 200 USDT 人数
     */
    private Integer twoH;

    /**
     * 直推人员个人合约账户 >= 100 USDT 人数
     */
    private Integer oneH;

    /**
     * 直推人员个人合约账户 > 50 USDT 人数
     */
    private Integer fifty;

    /**
     * 是否是人工干预账户
     */
    private  String intervene;

    /**
     * 团队交易总额
     */
    private BigDecimal total;

    /**
     * 直推人员vo
     */
    private List<RecommendVO>  list;

    /**
     * 三个月的团队交易总额
     */
    private BigDecimal measure;
}
