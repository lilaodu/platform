package com.blockchain.platform.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.OrderFlowEntity;
import com.blockchain.platform.pojo.entity.UserAdviceEntity;
import com.blockchain.platform.pojo.vo.UserVO;

/**
 * 撮合分发接口
 * @author zhangye
 *
 */
public interface IMatchDistributeService {

	/**
	 * 开始交易撮合
	 * @param entity
	 * @throws Exception
	 */
	void match(OrderFlowEntity entity) throws Exception;
}
