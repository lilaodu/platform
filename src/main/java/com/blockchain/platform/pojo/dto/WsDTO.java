package com.blockchain.platform.pojo.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * websocket 数据传输对象
 * @author zhangye
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WsDTO implements Serializable {

    /**
     * 当前用户token
     */
    private String token;

    /**
     * 当前请求数据
     */
    private String symbol;

}
