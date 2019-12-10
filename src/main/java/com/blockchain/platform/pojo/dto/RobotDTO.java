package com.blockchain.platform.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zjl
 * 人机验证参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RobotDTO {
    /**
     * gt参数
     */
    private String geetest_challenge;

    private String geetest_validate;

    private String geetest_seccode;

    /**
     * 服务状态
     */
    private Integer gt_server_status;

    /**
     * txHash 等同于 用户userid
     */
    private String txHash;

    /**
     * 验证ip
     */
    private String ip;
}
