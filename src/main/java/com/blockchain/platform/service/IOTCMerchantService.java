package com.blockchain.platform.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.entity.OTCMerchantEntity;
import com.blockchain.platform.pojo.vo.UserInfoVO;

/**
 * otc商户信息服务接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-19 4:04 PM
 **/
public interface IOTCMerchantService extends IService<OTCMerchantEntity> {

    /**
     * 商户详情
     * @param dto
     * @return
     */
    OTCMerchantEntity findByCondition(BaseDTO dto);

    /**
     * 申请成为商户
     * @param entity
     * @return
     */
    Boolean apply(OTCMerchantEntity entity);
}
