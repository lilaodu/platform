package com.blockchain.platform.pojo.vo;

import com.blockchain.platform.utils.BizUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 抽奖记录展示VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-15 2:10 PM
 **/
@Data
public class LuckDrawLogVO implements Serializable {

    /**
     * id
     */
    private Integer id;

    /**
     * 用户账号
     */
    private String username;

    /**
     *  用户头像
     */
    private String headImage;

    /**
     * 代币
     */
    private String symbol;

    /**
     *  数量
     */
    private String num;

    /**
     * 时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;

    /**
     * 格式化用户登录名
     */
    public String getUsername() {
       return BizUtils.formatName( username);
    }
}
