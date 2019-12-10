package com.blockchain.platform.pojo.vo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 工单展示VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-26 2:37 PM
 **/
@Data
public class WorkOrderVO implements Serializable {

    /**
     * id
     */
    private Integer id;

    /**
     * 问题
     */
    private String question;

    /**
     * 原因
     */
    private String reason;

    /**
     * 证明材料
     */
    private String material;

    /**
     * 证明材料列表
     */
    private List<String> materials;

    /**
     * 时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 工单详情
     */
    List<WorkOrderDetailsVO> details;

    /**
     * 处理证明材料
     * @return
     */
    public List<String> getMaterials() {
        return CollUtil.toList(StrUtil.split( material, StrUtil.COMMA));
    }
}
