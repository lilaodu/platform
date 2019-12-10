package com.blockchain.platform.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页显示VO
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-15 3:06 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageVO implements Serializable {

    /**
     * 总条数
     */
    private Long total;

    /**
     * 数据
     */
    private List<?> list;

}
