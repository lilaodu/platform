package com.blockchain.platform.pojo.vo;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.blockchain.platform.utils.BizUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户VO
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-14 11:38 AM
 **/
@Data
public class UserVO implements Serializable {

    /**
     * 用户token
     */
    private String token;

    /**
     * 用户账号
     */
    private String mobile;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户名
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
     * 用户角色
     */
    private String access;

    /**
     * 是否实名认证
     */
    private Integer verified;

    /**
     * 交易密码剩余时间
     * 24 小时之内不用输入
     */
    private Long expires;

    /**
     * 用户id
     */
    private Integer id;

    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 用户等级
     */
    private Integer sign;

    /**
     * 团队人数
     */
    private Integer teamNum;

    /**
     * 等级(普通用户，师长，军长等)
     */
    private String rank;

    /**
     * 是否是商户
     */
    private String isMerchant;

    /**
     * 格式化ID
     * @return
     */
    public String getUid() {
        return StrUtil.fillBefore( StrUtil.toString( id + 10000), '0', 7);
    }

    /**
     * 格式化电话号码，隐藏中间四位
     * @return
     */
    public String getMobile() {
        return StrUtil.isNotEmpty(mobile) ? StrUtil.hide( mobile, 3, 7) : mobile;
    }

    /**
     * 格式化邮箱，隐藏
     * @return
     */
    public String getEmail() {
        return StrUtil.isNotEmpty(email) ? email.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4") : email;
    }

    /**
     * 格式化用户登录名
     */
    public String getUsername() {
        return BizUtils.formatName( username);
    }
}
