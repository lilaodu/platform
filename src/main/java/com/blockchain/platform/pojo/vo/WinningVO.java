package com.blockchain.platform.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 中奖信息
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-05 2:37 PM
 **/
@Data
public class WinningVO implements Serializable {

    /**
     * 是否是自己
     */
    private boolean own = false;

    /**
     * 是否中奖
     */
    private String prize;

    /**
     * 中奖号码
     */
    private Integer number;

    /**
     * 通知类型
     */
    private String type;

    /**
     * 数据
     * 0 电话号码
     * 1 中奖数量
     * 2 中奖符号
     * 3 中奖时间
     */
    private List< List<Object>> bullet;
}
