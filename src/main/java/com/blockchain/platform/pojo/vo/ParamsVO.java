package com.blockchain.platform.pojo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 参数VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-16 12:24 AM
 **/
@Data
public class ParamsVO implements Serializable{

    /**
     * 参数类型
     */
    private String paramType;

    /**
     * 参数值
     */
    private String paramValue;
}
