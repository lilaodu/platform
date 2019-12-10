package com.blockchain.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.mapper.OTCMerchantMapper;
import com.blockchain.platform.mapper.OTCTrustMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.dto.TrustDTO;
import com.blockchain.platform.pojo.entity.OTCMerchantEntity;
import com.blockchain.platform.pojo.entity.OTCTrustEntity;
import com.blockchain.platform.pojo.vo.OTCTrustVO;
import com.blockchain.platform.service.IOTCTrustService;
import com.blockchain.platform.utils.IntUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * otc信任管理服务实现类
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-19 9:25 AM
 **/
@Service
public class OTCTrustServiceImpl extends ServiceImpl<OTCTrustMapper, OTCTrustEntity> implements IOTCTrustService {

    /**
     * 信任管理数据接口
     */
    @Resource
    private OTCTrustMapper mapper;

    /**
     * 商户数据接口
     */
    @Resource
    private OTCMerchantMapper merchantMapper;

    @Override
    public List<OTCTrustVO> query(PageDTO dto) {
        return mapper.query( dto);
    }

    @Override
    public Boolean modify(TrustDTO dto) {
        Integer bool = IntUtils.INT_ZERO;
        //获取被操作商户详情
        OTCMerchantEntity merchant = merchantMapper.findByCondition( BaseDTO.builder()
                                                                        .userId( dto.getPassiveUserId())
                                                                        .state( BizConst.BIZ_STATUS_VALID).build());
        //copy 属性
        OTCTrustEntity entity = BeanUtil.toBean( dto, OTCTrustEntity.class);
        //判断是新增还是取消
        if(StrUtil.equals( dto.getMethod(), BizConst.TrustConst.TRUST_ADD)){
            //新增
            bool = mapper.add( entity);
            //添加信任
            if(StrUtil.equals( dto.getType(), BizConst.TrustConst.TYPE_TRUST)){
                //被信任用户的信任次数 + 1
                merchant.setTrustedNum( IntUtils.toInt( NumberUtil.add( merchant.getTrustedNum(), IntUtils.INT_ONE)));
            }
        }else{
            //无效的状态
            entity.setState( BizConst.BIZ_STATUS_FAILED);
            //取消
            bool = mapper.update( entity);
            //取消信任
            if(StrUtil.equals( dto.getType(), BizConst.TrustConst.TYPE_TRUST)){
                //被信任用户的信任次数 - 1
                merchant.setTrustedNum( IntUtils.toInt( NumberUtil.sub( merchant.getTrustedNum(), IntUtils.INT_ONE)));
            }
        }
        return bool > 0;
    }
}
