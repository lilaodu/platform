package com.blockchain.platform.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * app版本VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-31 10:56 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VersionVO implements Serializable {

    /**
     * ios版本号
     */
    private String iosVersion;

    /**
     * ios下载地址
     */
    private String iosAddress;

    /**
     * android版本号
     */
    private String androidVersion;

    /**
     * android下载地址
     */
    private String androidAddress;

}
