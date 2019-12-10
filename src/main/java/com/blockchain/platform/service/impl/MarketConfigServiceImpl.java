package com.blockchain.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockchain.platform.mapper.MarketConfigMapper;
import com.blockchain.platform.pojo.entity.MarketConfigEntity;
import com.blockchain.platform.service.IMarketConfigService;
import org.springframework.stereotype.Service;

/**
 * 咨询建议留言接口
 *
 * @author ml
 * @version 1.0
 * @create 2019-07-17 1:43 PM
 **/
@Service
public class MarketConfigServiceImpl extends ServiceImpl<MarketConfigMapper, MarketConfigEntity> implements IMarketConfigService {

}
