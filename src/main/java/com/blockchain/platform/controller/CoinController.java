package com.blockchain.platform.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.OrderDTO;
import com.blockchain.platform.pojo.entity.CoinEntity;
import com.blockchain.platform.service.ICoinService;
import com.blockchain.platform.utils.ExUtils;

/**
 * 币控制类
  * @author zhangye
 *
 */
@RestController
@RequestMapping("/coin")
public class CoinController extends BaseController {

	@Resource
	ICoinService coinServer;
	/**
     * 币简介
     *
     */
    @RequestMapping("/info")
    public ResponseData info(@RequestBody @Valid BaseDTO dto, BindingResult valid, HttpServletRequest request) {
    	
    	ResponseData data = ResponseData.ok();
    	try {
    		QueryWrapper queryWrapper = new QueryWrapper();
    		queryWrapper.eq("symbol", dto.getSymbol());
    		CoinEntity coin = coinServer.getOne(queryWrapper);
    		data.setData(coin);
        } catch (Exception ex) {
        	ex.printStackTrace();
        	ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
        }
    	
		return data;
    }
	
	
}
