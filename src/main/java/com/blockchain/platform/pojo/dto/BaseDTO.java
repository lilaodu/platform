package com.blockchain.platform.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 基础DTO
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-15 1:06 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseDTO implements Serializable {

    /**
     * 基础ID
     */
    private Integer id;
    
    /**
     * 交易市场（交易对中的基础币）
     */
    private String market;

    /**
     * 类型
     */
    private String type;
    
    /**
     * 小数位数
     */
    private List<Integer> decimal;
    
    /**
     * 状态
     */
    private Integer state;

    /**
     * 限制条数
     */
    private Integer limit;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 语言
     */
    private String lang;

    /**
     * 交易货币
     */
    private String coin;
    
    /**
     * 当前交易对
     */
    private String symbol;

    /**
     * 广告编号
     */
    private String advertNumber;

    /**
     * 订单号
     */
    private String orderNumber;

    //查询订单时
    /**
     * 第几页
     */
    private Integer pageNo;
    /**
     * 每页大小
     */
    private Integer pageSiz;
    /**
     * 时间范围
     */
    private Integer dayScope;
    /**
     * 交易对
     */
    private String site;
    /**
     * 订单类型
     */
    private Integer orderType;
    /**
     * 当前委托或者历史记录
     */
    private Integer completionDegree;

    /**
     * 时间
     */
    private Date time;

    /**
     * 汇率
     */
    private String rate;

    /**
     * 数量
     */
    private  BigDecimal num;

    /**
     * 名称
     */
    private  String name;

    /**
     * 解仓数量
     */
    private BigDecimal unlockNum;

    /**
     * url
     */
    private String url;

    /**
     * 权限等级
     */
    private String authority;

    /**
     * 查询代币
     */
    private List<String> array;

    /**
     * 充币协议
     */
    private String protocol;

    /**
     * 提币hash
     */
    private String hash;

    /**
     * 资金密码
     */
    private String cipher;
}
