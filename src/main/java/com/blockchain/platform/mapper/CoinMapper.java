package com.blockchain.platform.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockchain.platform.pojo.entity.CoinEntity;

@Mapper
public interface CoinMapper extends BaseMapper<CoinEntity> {

}
