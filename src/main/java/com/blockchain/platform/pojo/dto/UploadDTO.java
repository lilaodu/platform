package com.blockchain.platform.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 文件传输DTO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 10:36 AM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadDTO implements Serializable {

    /**
     * 上传人ID
     */
    private Integer userId;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件类型
     */
    private String imgType;

    /**
     * 文件内容
     */
    private String img;
    /**
     * 文件内容
     */
    private String imageContent;

    /**
     * 路径类型
      */
    private String pathType;

    /**
     * 状态
     */
    private Integer state;
}
