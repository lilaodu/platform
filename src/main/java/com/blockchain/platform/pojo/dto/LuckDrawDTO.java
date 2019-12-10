package com.blockchain.platform.pojo.dto;

import com.blockchain.platform.utils.IntUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 抽奖服务数据传输类
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-15 6:25 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LuckDrawDTO implements Serializable {

    /**
     * 一等奖是否已经被抽
     */
    private  Integer first = IntUtils.INT_ZERO;

    /**
     * 最近40单中是否产生了1，2，3，4等奖
     */
    private Integer prize = IntUtils.INT_ZERO;

    /**
     * 今天抽中的奖项总和
     */
    private  Integer num = IntUtils.INT_ZERO;

    /**
     * 当前抽奖活动的当日代币可抽总量
     */
    private Integer total;

    public Integer getNum() {
        return IntUtils.isZero( num)? 0 : num;
    }

}
