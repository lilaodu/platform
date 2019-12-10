package com.blockchain.platform.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 定时任务服务接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-26 10:13 AM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO implements Serializable {

    /**
     * 查询时间
     */
    private String  time;


    /**
     * 权限
     */
    private String authority;

    /**
     * 结束时间
     */
    private String end;


    /**
     * 货币
     */
    private List<String> symbol;


    private Integer userId;

}
