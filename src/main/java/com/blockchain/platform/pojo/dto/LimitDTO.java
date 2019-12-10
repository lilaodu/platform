package com.blockchain.platform.pojo.dto;

import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.utils.IntUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 限制查询DTO
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-15 1:09 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LimitDTO implements Serializable {

    private Integer limit;

    public Integer getLimit() {
        return IntUtils.isEmpty( limit) ?  AppConst.DEFAULT_LIMIT_SIZE : limit;
    }

}
