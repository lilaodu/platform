package com.blockchain.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户实体对象
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-14 11:37 AM
 **/
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_user")
public class UserEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话号码
     */
    private String mobile;

    /**
     * 密码
     */
    private String password;

    /**
     * 头像
     */
    private String headImage;

    /**
     * 上一级id(邀请人id)
     */
    private Integer parentId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 邀请码
     */
    private String invitationCode;

    /**
     * ip地址
     */
    private String ip;

    /**
     *  邮箱检查
     */
    private String emailCheck;

    /**
     * 电话号码检查
     */
    private String mobileCheck;

    /**
     * 语言
     */
    private String lang;

    /**
     * 费率
     */
    private BigDecimal feeRate;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * >=100：管理员 200：收款人 110：总经理 120：总监 130：销售
     */
    private Integer sign;

    /**
     * 绑定电话
     */
    private String bindMobile;

    /**
     * 绑定邮箱
     */
    private String bindEmail;

    /**
     * 谷歌两步验证
     */
    private String google;

    /**
     * 实名认证
     */
    private Integer verified;

    /**
     * 谷歌两步验证二维码
     */
    private String qrCode;

    /**
     * 谷歌两步验证KEY
     */
    private String secret;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 真实邮箱
     */
    private String realEmail;

    /**
     *
     */
    private Integer oldId;

    /**
     * api_key
     */
    private String apiKey;

    /**
     * api_secret
     */
    private String apiSecret;

    /**
     * 是否启用API
     */
    private String enabledApi;

    /**
     * API添加时间
     */
    private Date apiDate;

    /**
     * 权限
     */
    private String authority;

    /**
     * 资金密码
     */
    private String cipher;

    /**
     * 秒合约等级
     */
    private String rank;

    /**
     * 用户最多可同时押注秒合约的单数（默认5）
     */
    private int secondsContractNum;

    /**
     * 星球计划等级
     */
    private int lockLv;
}
