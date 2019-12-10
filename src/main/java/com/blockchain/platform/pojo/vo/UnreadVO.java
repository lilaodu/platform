package com.blockchain.platform.pojo.vo;

import cn.hutool.core.date.DateUtil;
import com.blockchain.platform.utils.IntUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息未读VO
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-26 3:08 PM
 **/
@Data
public class UnreadVO implements Serializable {

    /**
     * 通知时间
     */
    private Long timestamp = DateUtil.currentSeconds();


    /**
     * 未读数量
     */
    private Integer count = IntUtils.INT_ZERO;
}
