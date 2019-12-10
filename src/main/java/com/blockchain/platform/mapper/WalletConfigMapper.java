package com.blockchain.platform.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockchain.platform.pojo.entity.WalletConfigEntity;

@Mapper
public interface WalletConfigMapper extends BaseMapper<WalletConfigEntity> {

}
