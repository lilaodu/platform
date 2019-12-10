package com.blockchain.platform.pojo.vo;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 实名认证展示VO
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 4:15 PM
 **/
@Data
@JsonInclude( JsonInclude.Include.NON_NULL)
public class UserKycVO {

    /**
     * 名
     */
    private String firstName;

    /**
     * 姓
     */
    private String lastName;

    /**
     * 国家
     */
    private String country;

    /**
     * 证件类型
     */
    private String idType;

    /**
     * 证件号码
     */
    private String idNo;

    /**
     * 持证照
     */
    private String picHold;

    /**
     * 正面照
     */
    private String picFace;

    /**
     * 背面照
     */
    private String picBack;

    /**
     * 状态
     */
    private String state;

    /**
     * 当前进度
     */
    private Integer step;

    /**
     * 审核理由
     */
    private String remark;

    /**
     * 格式化证件号
     * @return
     */
    public String getIdNo() {
        return StrUtil.isNotEmpty(idNo) ? StrUtil.hide( idNo, 7, 14) : StrUtil.EMPTY;
    }
}
