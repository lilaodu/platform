package com.blockchain.platform.pojo.vo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.entity.OTCOrderEntity;
import com.blockchain.platform.pojo.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * OTC订单显示VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-22 4:03 PM
 **/
@Data
public class OTCOrderVO implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 类型
     */
    private String type;

    /**
     * 货币简称
     */
    private String symbol;

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 广告编号
     */
    private String advertNumber;

    /**
     * 订单用户id
     */
    private Integer userId;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 数量
     */
    private BigDecimal num;

    /**
     * 报价
     */
    private BigDecimal price;

    /**
     * 时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 汇率
     */
    private String rate;

    /**
     * 订单用户
     */
    private String username;

    /**
     * 商家
     */
    private String merchant;

    /**
     * 订单用户昵称
     */
    private  String userNick;

    /**
     * 卖家昵称
     */
    private String merchantNick;

    /**
     * 订单用户实名
     */
    private String userReal;

    /**
     * 商家实名
     */
    private String merchantReal;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 支付账号
     */
    private String payAccount;

    /**
     * 收款账号
     */
    private String receiveAccount;

    /**
     * 备注码
     */
    private String remarkCode;

    /**
     * 是否有新消息
     * > 0 有
     */
    private Integer chatNum = 0;

    /**
     * 当前时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date nowTime = new Date();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    /**
     * 用户头像
     */
    private String headImage;

    /**
     * 结束时间
     * @return
     */


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;


    long remindTime=0;

    String buyerName;
    String sellerName;

    OTCOrderEntity order;
    String bankName;
    private String merchantImage;
    String buyerTel;
    String sellerTel;
    String buyerImage;
    String sellerImage;


}
