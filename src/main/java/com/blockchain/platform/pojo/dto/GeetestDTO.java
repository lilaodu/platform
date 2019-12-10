package com.blockchain.platform.pojo.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 极验初始化DTO
 * @author zjl
 */
@Data
@Builder
public class GeetestDTO implements Serializable {
    /**
     * 验证用户
     */
    private String user_id;

    /**
     * 验证类型
     */
    private String client_type;

    /**
     * 验证IP
     */
    private String ip_address;

}
