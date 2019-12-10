package com.blockchain.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.vo.UserFavCoinVO;
import com.blockchain.platform.service.IOrderFlowService;
import com.blockchain.platform.service.IUserService;

import com.blockchain.platform.utils.IntUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.MarketConfigEntity;
import com.blockchain.platform.pojo.entity.SecondsContractEntity;
import com.blockchain.platform.pojo.vo.RankingVO;
import com.blockchain.platform.utils.ExUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 交易行情数据展示控制器
 *
 * @author zhangye
 **/
@RestController
@RequestMapping("/quotation")
public class QuotationController extends BaseController {

    /**
     * 用户服务接口
     */
    @Resource
    private IUserService userService;

    /**
     * 查询 交易行情排行
     * @param dto
     * @return
     */
    @RequestMapping("/ranking")
    public ResponseData ranking(@RequestBody PageDTO dto) {
        ResponseData data = ResponseData.ok();
        try {
            //返回列表
            List<RankingVO> voList = CollUtil.newArrayList();
            if(StrUtil.isNotEmpty( dto.getMarket())){
                //获取交易市场对应的货币排行
                voList = redisPlugin.hget(RedisConst.PLATFORM_RANKING, dto.getMarket());

            }else{
                //首页数据
                Map<String, List<RankingVO>> home = redisPlugin.get( RedisConst.PLATFORM_HOME_RANKING);
                //返回对应的列表数据
                voList = home.get( dto.getType());
            }
            //返回数据
            if(BeanUtil.isEmpty(dto.getLimit())) {
            	data.setData( voList);
            }else {
            	data.setData( CollUtil.sub( voList, IntUtils.INT_ZERO, dto.getLimit()));
            }
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 获取用户自选货币的排行榜
     * @param request
     * @return
     */
    @RequestMapping("/optional")
    public ResponseData optional(HttpServletRequest request){
        ResponseData data = ResponseData.ok();
        try {
        	
            //登录用户
            UserDTO user = getLoginUser( request);
            if(BeanUtil.isEmpty(user)) {
            	throw ExUtils.error(  LocaleKey.USER_NOT_FIND);
            }
        	//获取用户自选的数据
            List<UserFavCoinVO> vos = userService.favorTokens( BaseDTO.builder()
                                                        .userId(user.getId())
                                                        .state(BizConst.BIZ_STATUS_VALID).build());
            //返回数据
            Map<String, List<RankingVO>> map = MapUtil.newHashMap();
            for( int idx = 0; idx < vos.size(); idx ++) {
                //列表
                List<RankingVO> voList = CollUtil.newArrayList();
                //判断是否已存在交易市场
                if( map.containsKey( vos.get( idx).getMarket())) {
                    voList = map.get(vos.get(idx).getMarket());
                }
                //获取排行榜的数据
                List<RankingVO> list = redisPlugin.hget( RedisConst.PLATFORM_RANKING, vos.get( idx).getMarket());
                if(CollUtil.isNotEmpty( list)){
                    //遍历数据
                    for( int index = 0; index < list.size(); index ++){
                        if( StrUtil.equals( vos.get( idx).getSymbol(), list.get( index).getSymbol())){
                            voList.add( list.get( index));
                        }
                    }
                    map.put( vos.get( idx).getMarket(), voList);
                }
            }
            data.setData( map);
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }
    
    /**
     * 获取所有市场
     * @return
     */
    @RequestMapping("/markets")
    public ResponseData markets() {
        ResponseData data = ResponseData.ok();
//        List<Object> market = new ArrayList<Object>();
        try {
        	List<MarketConfigEntity> markets = redisPlugin.get(RedisConst.MARKET);
//        	market.add(markets);//所有市场
        	
//        	Map<Integer, SecondsContractEntity> contractMarkets = redisPlugin.get(RedisConst.SECONDS_CONTRACT_CONFIG);
//        	List<SecondsContractEntity> contracts = new ArrayList<SecondsContractEntity>();
//        	for(SecondsContractEntity value : contractMarkets.values()){
//        		contracts.add(value);//秒合约市场
//        	}
//        	market.add(contracts);
            data.setData(markets);

        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }
}
