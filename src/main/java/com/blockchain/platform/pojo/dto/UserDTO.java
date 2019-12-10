package com.blockchain.platform.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户DTO
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-14 11:36 AM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {

    /**
     * 用户唯一主键
     */
    private Integer id;

    /**
     * 父级id
     */
    private Integer parentId;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 头像
     */
    private String headImage;

    /**
     * 邀请码
     */
    private String invitationCode;

    /**
     * 资金密码
     */
    private String cipher;

    /**
     * 密钥
     */
    private String secret;

    /**
     * apiKey
     */
    private String key;

    /**
     * 登录IP
     */
    private String ip;

    /**
     * 交易密码剩余时间
     * 24 小时之内不用输入
     */
    private Long expires;

    /**
     * 查询状态
     */
    private Integer state;

    /**
     * 个人手续费比例
     */
    private BigDecimal feeRate;

    /**
     * 是否kyc
     */
    private Integer verified;

    /**
     * 是否是商户
     */
    private String isMerchant;

    /**
     * 商户id
     */
    private Integer merchantId;

}