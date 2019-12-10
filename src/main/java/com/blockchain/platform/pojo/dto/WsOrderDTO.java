package com.blockchain.platform.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import com.blockchain.platform.pojo.vo.OrderVO;

/**
 * WS传输DTO
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-05-19 12:16 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WsOrderDTO implements Serializable {

    /**
     * 小数位数
     */
    private Integer digit;

    /**
     * 订单数据
     */
    private List<OrderVO> list;

}
