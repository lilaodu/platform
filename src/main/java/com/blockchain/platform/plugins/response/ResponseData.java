package com.blockchain.platform.plugins.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 统一返回对象
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-15 2:17 PM
 **/
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData implements Serializable {

    /**
     * 请求状态
     */
    private Integer code;

    /**
     * 是否请求成功
     */
    private Boolean success = Boolean.TRUE;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 返回数据
     */
    private Object data;

    /**
     * 初始化成功
     * @return
     */
    public static final ResponseData ok(){
        return ResponseData.builder().code( HttpStatus.OK.value()).build();
    }

    /**
     * 错误信息
     * @return
     */
    public static final ResponseData fail(){
        return ResponseData.builder().code( HttpStatus.BAD_REQUEST.value()).build();
    }
}
