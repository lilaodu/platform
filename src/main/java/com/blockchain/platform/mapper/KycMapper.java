
package com.blockchain.platform.mapper;


import com.blockchain.platform.pojo.dto.BaseDTO;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockchain.platform.pojo.entity.UserKycEntity;


@Mapper
public interface KycMapper extends BaseMapper<UserKycEntity> {

    /**
     * kyc 详情
     * @param dto
     * @return
     */
    UserKycEntity findByCondition(BaseDTO dto);

    /**
     * ç1 认证
     * @param entity
     * @return
     */
    Integer add(UserKycEntity entity);

    /**
     * C2 认证
     * @param entity
     * @return
     */
    Integer update(UserKycEntity entity);
}
