package com.blockchain.platform.pojo.vo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * OTC广告显示VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-18 5:34 PM
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL )
public class OTCAdvertVO implements Serializable {


    BigDecimal remindNum;
    /**
     * 主键
     */
    private Integer id;

    /**
     * 商户头像
     */
    private String headImage = StrUtil.EMPTY;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 广告类型
     */
    private String type;

    /**
     * 广告编号
     */
    private String advertNumber;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 代币
     */
    private String symbol;

    /**
     * 限额（上限）
     */
    private BigDecimal limitUp;

    /**
     * 限额（下限）
     */
    private BigDecimal limitDown;

    /**
     * 汇率
     */
    private String rate;

    /**
     * 付款方式
     */
    private String payType;

    /**
     * 付款方式
     */
    private List<String> payList;






    /**
     * 状态
     */
    private Integer state;

    /**
     * 留言备注
     */
    private String remark;

    /**
     * 商户id
     */
    private Integer merchantId;

    /**
     * 商户昵称
     */
    private String nickName = StrUtil.EMPTY;

    /**
     * 是否在线
     */
    private String online;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 头像
     */
    private String headImg;

    /**
     * 获取支付方式
     */
    public void setPayList() {
        this.payList = CollUtil.toList(payType.split(StrUtil.COMMA));
    }

    /**
     * 设置支付方式
     * @return
     */
    public List<String> getPayList() {
        return CollUtil.toList(payType.split(StrUtil.COMMA));
    }
}
