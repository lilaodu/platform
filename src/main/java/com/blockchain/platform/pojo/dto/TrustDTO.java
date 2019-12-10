package com.blockchain.platform.pojo.dto;

import com.blockchain.platform.i18n.LocaleKey;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * OTC信任管理 数据传输DTO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-19 10:04 AM
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY )
public class TrustDTO implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 类型（shield：屏蔽，trust：信任,accusation：举报）
     */
    @NotBlank(message = LocaleKey.SYS_PARAM_ERROR)
    private String type;

    /**
     * 方法：添加/取消
     */
    private String method;

    /**
     * 广告id
     */
    private Integer advertId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 被屏蔽/信任/举报的用户
     */
    private Integer passiveUserId;

    /**
     * 举报原因
     */
    private String reason;

    /**
     * 举报内容
     */
    private String content;
}
