package com.blockchain.platform.service.impl;

import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.utils.IntUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockchain.platform.mapper.KycMapper;
import com.blockchain.platform.pojo.entity.UserKycEntity;
import com.blockchain.platform.service.IKycService;

import javax.annotation.Resource;

@Service
public class KycServiceImpl extends ServiceImpl<KycMapper, UserKycEntity> implements IKycService {

    /**
     * kyc 数据库接口
     */
    @Resource
    private KycMapper mapper;

    @Override
    public UserKycEntity findByCondition(BaseDTO dto) {
        return mapper.findByCondition( dto);
    }

    @Override
    public Boolean modify(UserKycEntity entity) {
        //判断是C1还是C2认证
        if( IntUtils.isZero( entity.getId())){
            return saveOrUpdate( entity);
        }else{
            return mapper.update( entity) > 0;
        }
    }
}
