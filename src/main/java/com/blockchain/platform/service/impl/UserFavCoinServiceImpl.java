package com.blockchain.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockchain.platform.mapper.AdviceMapper;
import com.blockchain.platform.mapper.UserFavCoinMapper;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.FavCoinDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.UserAdviceEntity;
import com.blockchain.platform.pojo.entity.UserFavCoinEntity;
import com.blockchain.platform.pojo.vo.UserAdviceVO;
import com.blockchain.platform.service.IAdviceService;
import com.blockchain.platform.service.IUserFavCoinService;
import com.blockchain.platform.utils.IntUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserFavCoinServiceImpl extends ServiceImpl<UserFavCoinMapper, UserFavCoinEntity> implements IUserFavCoinService {

	@Resource
	private UserFavCoinMapper mapper;

	
	
	
	public UserFavCoinEntity findFavByCondition(FavCoinDTO dto) {
        return mapper.findFavByCondition( dto);
    }

    
}
