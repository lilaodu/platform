package com.blockchain.platform.pojo.vo;

import cn.hutool.core.util.StrUtil;

import com.blockchain.platform.utils.IntUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 货币前端查询VO
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-05-18 1:59 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenVO implements Serializable {

    /**
     * 货币简称
     */
    private String symbol;

    /**
     * 货币图片
     */
    private String icon;

    /**
     * 货币全称
     */
    private String coinfullname;


    /**
     * 最小转入
     */
    private BigDecimal lowerlimitin;

    /**
     * 最小提现
     */
    private BigDecimal lowerlimitout;

    /**
     * 提现审核 > 0 审核
     */
    private BigDecimal audit;

    /**
     * 小数位数
     */
    private String declength;

    /**
     * 最小可买
     */
    private BigDecimal minbuy;

    /**
     * 最大可买
     */
    private BigDecimal maxbuy;

    /**
     * 允许转入
     */
    private String allowin;

    /**
     * 允许转出
     */
    private String allowout;

    /**
     * 充值钱包地址
     */
    private String walletAddress;

    /**
     * 流通量
     */
    private String circulation;

    /**
     * 发行价 ￥
     */
    private BigDecimal price;

    /**
     * 官网
     */
    private String website;

    /**
     * 白皮书地址
     */
    private String whitepaper;

    /**
     * 货币简介
     */
    private String explain;

    /**
     * 发行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date issueTime;

    /**
     * 发行量
     */
    private String issue;

    /**
     * 区块链查询地址
     */
    private String blockUrl;

    /**
     * 中文名称
     */
    private String chineseName;

    /**
     * 是否为基础货币
     */
    private String isbasics;

    /**
     * 转出手续费
     */
    private BigDecimal outfee;

    /**
     * 手续费代币
     */
    private String feesymbol;

    /**
     * 小数位数
     */
    private List<Integer> digit;

    public List<Integer> getDigit() {
        List<Integer> value = new ArrayList<>();
        if (StrUtil.isNotEmpty( declength)) {
            for (String str : Arrays.asList( StrUtil.split( declength, StrUtil.COMMA))){
                value.add(IntUtils.toInt( str));
            }
        }
        return value;
    }
}
