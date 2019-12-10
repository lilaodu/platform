package com.blockchain.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.entity.UserKycEntity;

import java.io.Serializable;

/**
 * KYC服务器接口
 */
public interface IKycService extends IService<UserKycEntity> {


    /**
     * kyc 详情
     * @param dto
     * @return
     */
    UserKycEntity findByCondition(BaseDTO dto);

    /**
     * 用户C1 或 C2认证
     * @param entity
     * @return
     */
    Boolean modify(UserKycEntity entity);
}

