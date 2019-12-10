package com.blockchain.platform.pojo.dto;

import com.blockchain.platform.i18n.LocaleKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * otc广告实体对象
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-18 11:48 AM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OTCRepresentationDTO implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 订单编号
     */
    @NotBlank(message = LocaleKey.SYS_PARAM_ERROR)
    private String orderNumber;

    /**
     * 申述类型
     */
    private String type;

    /**
     * 申述理由
     */
    private String reason;

    /**
     * 证明材料
     */
    private String material;
}
