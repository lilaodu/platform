package com.blockchain.platform.pojo.vo;

import com.blockchain.platform.pojo.entity.UserWalletEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 基础信息展示VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-24 3:27 PM
 **/
@Data
public class UserInfoVO implements Serializable {

    /**
     * 发布的广告数
     */
    private Integer advertNum;

    /**
     * 用户钱包信息
     */
    private UserWalletEntity wallet;

}
