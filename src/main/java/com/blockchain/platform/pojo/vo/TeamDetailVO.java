package com.blockchain.platform.pojo.vo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 团队明细VO
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-21 6:52 AM
 **/
@Data
public class TeamDetailVO implements Serializable {


    /**
     * 邮箱
     */
    private String email;


    /**
     * 电话号码
     */
    private String mobile;


    /**
     * 是否实名
     *
     * 6 已实名
     */
    private Integer verified;


    public String getEmail() {
        return StrUtil.isNotEmpty(email) ? email.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4") : email;
    }

    public String getMobile() {
        return StrUtil.isNotEmpty(mobile) ? StrUtil.hide( mobile, 3, 7) : mobile;
    }

    /**
     * 团队明细
     */
    List<TeamDetailVO> list = CollUtil.newArrayList();
}
