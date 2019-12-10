package com.blockchain.platform.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * kline 视图
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TvChartVO implements Serializable {

    /**
     * 结果
     */
    private String s;

    /**
     * 数据
     */
    private List<Number[]> list;
}
