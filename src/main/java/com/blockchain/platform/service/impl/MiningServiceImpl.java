package com.blockchain.platform.service.impl;

import com.blockchain.platform.mapper.UserMapper;
import com.blockchain.platform.pojo.entity.OrderFlowEntity;
import com.blockchain.platform.service.IMiningService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;


@Service
public class MiningServiceImpl implements IMiningService {
	
    @Resource
    private UserMapper userMapper;

	
	public void doYesterdayTradeFee() {
		
		
	}
	
	public void doDigAssets(OrderFlowEntity event) {
		
		
	}

}
