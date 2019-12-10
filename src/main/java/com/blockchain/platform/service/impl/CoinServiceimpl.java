package com.blockchain.platform.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockchain.platform.mapper.CoinMapper;
import com.blockchain.platform.mapper.KycMapper;
import com.blockchain.platform.pojo.entity.CoinEntity;
import com.blockchain.platform.pojo.entity.UserKycEntity;
import com.blockchain.platform.service.ICoinService;
import com.blockchain.platform.service.IKycService;

@Service
public class CoinServiceimpl extends ServiceImpl<CoinMapper, CoinEntity> implements ICoinService {

}
