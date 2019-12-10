package com.blockchain.platform.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 图片传输
 * @author DENGLONG
 * @version 1.0
 * @create 2019-06-01 2:44 PM
 **/
@Data
public class PicDTO implements Serializable {

    /**
     * 类型
     */
    private String name;

    /**
     * 图片内容
     */
    private MultipartFile file;

}
