package com.blockchain.platform.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * OTC信任管理显示VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-19 9:15 AM
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY )
public class OTCTrustVO implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 类型
     */
    private String type;
}
