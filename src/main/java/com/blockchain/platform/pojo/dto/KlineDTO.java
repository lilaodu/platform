package com.blockchain.platform.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 查询k线对象
 * @author zhangye
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KlineDTO implements Serializable {

    /**
     * 开始
     */
    private Long from;

    /**
     * 结束
     */
    private Long to;

    /**
     * 分辨率
     */
    private Long resolution;

    /**
     * 当前标签
     */
    private String symbol;

}
