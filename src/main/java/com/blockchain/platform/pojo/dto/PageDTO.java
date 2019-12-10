package com.blockchain.platform.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

import com.blockchain.platform.constant.AppConst;


/**
 * 分页查询DTO
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-15 1:09 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO implements Serializable {

    /**
     * id
     */
    private Integer id;

    /**
     * 交易市场
     */
    private String market;
    
    /**
     * 每页显示数量
     */
    private Integer pageSize = AppConst.DEFAULT_PAGE_SIZE;

    /**
     * 页码
     */
    private Integer pageNum = AppConst.DEFAULT_PAGE_NO;
    
    /**
     * 查询时间 开始
     */
    private String start;
    
    /**
     * 查询时间 截止
     */
    private String end;

    /**
     * 类型
     */
    private String type;
    
    /**
     * 0:币币
     * 1:合约
     */
    private Integer chooseType;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 广告id
     */
    private Integer advertId;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 货币符号
     */
    private String symbol;
    
    /**
     * 交易对
     */
    private String coinPair;

    /**
     * 状态
     */
    private Integer state;
    
    /**
     * 是否分页
     */
    private Boolean paging;
    
    /**
     * 查询范围
     */
    private String scope;
    
    /**
     * 序号，前端发的订单种类，委托、历史
     */
    private Integer tabIndex;

    /**
     * 评论类型
     */
    private String opinion;

    /**
     * 主题
     */
    private String title;


    /**
     * 支付类型
     */
    private String payType;

    /**
     * 法币(汇率)
     */
    private String rate;
    
    /**
     * 条数
     */
    private Integer limit;

}
