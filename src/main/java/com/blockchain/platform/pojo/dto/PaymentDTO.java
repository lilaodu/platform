package com.blockchain.platform.pojo.dto;

import com.blockchain.platform.i18n.LocaleKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户支付方式DTO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 9:37 AM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO implements Serializable {

    /**
     * id
     */
    private Integer id;

    /**
     * 支付方式
     */
    @NotBlank(message = LocaleKey.PAY_TYPE_IS_NULL)
    private String payType;

    /**
     * 真实姓名
     */
    private String username;

    /**
     * 账户
     */
    private String account;

    /**
     * 二维码地址 或 开户行地址
     */
    private String address;

    /**
     * 开户行
     */
    private String bankName;

    /**
     * 支付方式名称
     */
    private String payName;

    /**
     * 图片类型
     */
    private String imgType;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 图片内容
     */
    private String imageContent;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 图片路径
     */
    private String pathType;
}
