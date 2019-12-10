package com.blockchain.platform.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 提币地址管理DTO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 12:39 AM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChequeDTO implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 货币简称
     */
    @NotEmpty
    private String token;

    /**
     * 提币自动
     */
    @NotEmpty
    private String address;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 交易密码
     */
    @NotEmpty
    private String password;

    /**
     * 短信验证码
     */
    @NotEmpty
    @Size(min = 4)
    private String sms;

    /**
     * 邮箱验证码
     */
    @NotEmpty
    @Size(min = 4)
    private String eml;

}
