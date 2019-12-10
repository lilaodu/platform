package com.blockchain.platform.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.entity.OTCMerchantEntity;
import com.blockchain.platform.pojo.vo.UserInfoVO;
import org.apache.ibatis.annotations.Mapper;


/**
 * otc商户接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-18 5:25 PM
 **/
@Mapper
public interface OTCMerchantMapper extends BaseMapper<OTCMerchantEntity> {

    /**
     * 商户详情
     * @param dto
     * @return
     */
    OTCMerchantEntity findByCondition(BaseDTO dto);

    /**
     * 新增商户
     * @param entity
     * @return
     */
    Integer add(OTCMerchantEntity entity);

    /**
     * 编辑商户信息
     * @param entity
     * @return
     */
    Integer update(OTCMerchantEntity entity);
}
