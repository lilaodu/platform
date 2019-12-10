package com.blockchain.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockchain.platform.mapper.OTCMerchantMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.entity.OTCMerchantEntity;
import com.blockchain.platform.pojo.vo.UserInfoVO;
import com.blockchain.platform.service.IOTCMerchantService;
import com.blockchain.platform.utils.IntUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * otc商户信息服务实现类
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-19 4:05 PM
 **/
@Service
public class OTCMerchantServiceImpl extends ServiceImpl<OTCMerchantMapper, OTCMerchantEntity> implements IOTCMerchantService {

    /**
     * otc广告数据接口
     */
    @Resource
    private OTCMerchantMapper mapper;


    @Override
    public OTCMerchantEntity findByCondition(BaseDTO dto) {
        return mapper.findByCondition( dto);
    }


    @Override
    public Boolean apply(OTCMerchantEntity entity) {
        if(IntUtils.isZero( entity.getId())){
            return mapper.add( entity) > 0;
        }else{
            //修改成为商户的时间
            entity.setMerchantTime( new Date());
            return  mapper.update( entity) > 0;
        }
    }
}
