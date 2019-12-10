package com.blockchain.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.mapper.OTCChatMapper;
import com.blockchain.platform.mapper.OrderFlowMapper;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.dto.WsChatDTO;
import com.blockchain.platform.pojo.dto.WsOrderDTO;
import com.blockchain.platform.pojo.entity.CoinEntity;
import com.blockchain.platform.pojo.entity.OTCChatEntity;
import com.blockchain.platform.pojo.vo.*;
import com.blockchain.platform.service.ISocketService;
import com.blockchain.platform.service.IUserWalletService;
import com.blockchain.platform.utils.BizUtils;
import com.blockchain.platform.utils.IntUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

/**
 * 消息通知实现类
 *
 * @author zhangye
 **/
@Service
public class SocketServiceImpl implements ISocketService {

    /**
     * 缓存数据
     */
    @Resource
    private RedisPlugin redisPlugin;

    /**
     * otc聊天数据接口
     */
    @Resource
    private OTCChatMapper chatMapper;

    /**
     * 订单数据接口
     */
    @Resource
    private OrderFlowMapper orderMapper;
    
    /**
     * 资产数据接口
     */
    @Resource
    private IUserWalletService walletService;
    
    
    /**
     * 获取 web端 登录用户
     * @param token
     * @return
     */
    protected UserDTO getLoginUser(String token) {
        if( !StrUtil.isEmpty( token)){
            return redisPlugin.hget(RedisConst.PLATFORM_USER_DATA, token);
        }
        return null;
    }

	
//	public WsVO findWsVOByCondition(String symbol, String token) {
//		
//		// 货币配置token
//        Map<String, CoinEntity> config = redisPlugin.get( RedisConst.PLATFORM_COIN_CONFIG);
//        if ( MapUtil.isEmpty( config) && config.containsKey(BizUtils.token(symbol))) {
//            return null;
//        }
//        CoinEntity entity = config.get( BizUtils.market(symbol));
//        // 当前货币 数据位数
//        List<String> digit = Arrays.asList( StrUtil.split( entity.getDecLength(), StrUtil.COMMA));
//        List<Integer> decimal = new ArrayList<>();
//        for (String d : digit) {
//            decimal.add( IntUtils.toInt( d));
//        }
//        // 交易行情
//        RankingVO ranking = getRankingVO( symbol);
//        // 查询对象
//        BaseDTO baseDTO = BaseDTO.builder().build();
//        baseDTO.setLimit( BizConst.OrderConst.DEFAULT_HANDICAP_LENGTH);
//        baseDTO.setSymbol( symbol);
//        // 买卖盘数据 状态 为 挂单
//        baseDTO.setState( BizConst.TradeConst.ORDER_TYPE_PENDING);
//        baseDTO.setDecimal( decimal);
//        // 买盘
//        baseDTO.setType( BizConst.TradeConst.TRADE_TYPE_INPUTS_EN);
//        Map<String, List<OrderVO>> buys = getHandicapVO( baseDTO);
//        // 卖盘
//        baseDTO.setType( BizConst.TradeConst.TRADE_TYPE_OUTS_EN);
//        Map<String, List<OrderVO>> sells = getHandicapVO( baseDTO);
//        // 成交记录
//        baseDTO.setLimit( BizConst.OrderConst.DEFAULT_HISTORY_LENGTH);
//        List<DealVO> history = getHistoryVO( baseDTO);
//
//        // 返回数据
//        WsVO vo = WsVO.builder().build();
//        vo.setHistory( history);//
//        vo.setBuys( buys);//
//        vo.setSells( sells);//
//        // 交易行情
//        vo.setTick( ranking);//
//        // 当前登录用户
//        UserDTO user = StrUtil.equals(token, StrUtil.EMPTY) ? null : getLoginUser( token);
//
//        if ( NumberUtil.isInteger( token)) {
//            user = UserDTO.builder().id( IntUtils.toInt( token)).build();
//        }
//
//        // 不为空 获取 订单数据
//        if ( ObjectUtil.isNotEmpty( user)) {
//            baseDTO.setUserId( user.getId());
//            baseDTO.setState( BizConst.BIZ_STATUS_VALID);
//            // 我的资产数据
//            Map<String, CapitalVO> capital = getCapitalVO( baseDTO);
//            vo.setCapital( capital);
//
//            PageDTO pageDTO = PageDTO.builder()
//                            .symbol( symbol)
//                            .pageNum( AppConst.DEFAULT_PAGE_NO)
//                            .pageSize( AppConst.DEFAULT_LIMIT_SIZE ) // app 端只显示 五条数据
//                            .userId( user.getId()).build();
//
//            Page<OrderVO> info = PageHelper.startPage( pageDTO.getPageNum(), pageDTO.getPageSize(), Boolean.TRUE);
//            List<OrderVO> list = orderMapper.queryUserDelegate( pageDTO);
//            // 委托订单
//            PageVO actives = PageVO.builder()
//                                    .list( list)
//                                    .total( info.getTotal()).build();
//            vo.setActives( actives);
//        }
//        return vo;
//	}
	/**
	 * k线买卖盘订阅信息
	 */
	public List<Map<String, List<OrderVO>>> findWsKByCondition(String symbol) {
		
		// 货币配置
        Map<String, CoinEntity> config = redisPlugin.get( RedisConst.PLATFORM_COIN_CONFIG);
        if ( MapUtil.isEmpty( config) && config.containsKey(BizUtils.token(symbol))) {
            return null;
        }
        CoinEntity entity = config.get( BizUtils.market(symbol));
        // 当前货币 数据位数
        List<String> digit = Arrays.asList( StrUtil.split( entity.getDecLength(), StrUtil.COMMA));
        List<Integer> decimal = new ArrayList<>();
        //只取第零位
        decimal.add( IntUtils.toInt(digit.get(0) ));
        // 查询对象
        BaseDTO baseDTO = BaseDTO.builder().build();
        baseDTO.setLimit( BizConst.OrderConst.DEFAULT_HANDICAP_LENGTH);
        baseDTO.setSymbol( symbol);
        // 买卖盘数据 状态 为 挂单
        baseDTO.setState( BizConst.TradeConst.ORDER_TYPE_PENDING);
        baseDTO.setDecimal( decimal);
        // 买盘
        baseDTO.setType( BizConst.TradeConst.TRADE_TYPE_INPUTS_EN);
        Map<String, List<OrderVO>> buys = getHandicapVO( baseDTO);
        // 卖盘
        baseDTO.setType( BizConst.TradeConst.TRADE_TYPE_OUTS_EN);
        Map<String, List<OrderVO>> sells = getHandicapVO( baseDTO);
        
        return new ArrayList<Map<String, List<OrderVO>>>() {{
    		add(buys);
        	add(sells);
    	}};
	}
	/**
	 * 交易历史订阅信息
	 */
	public List<DealVO> findWsHistoryByCondition(String symbol) {
		
        // 查询对象
        BaseDTO baseDTO = BaseDTO.builder().build();
        baseDTO.setSymbol( symbol);
        // 成交记录
        baseDTO.setLimit( BizConst.OrderConst.DEFAULT_HISTORY_LENGTH);
        List<DealVO> history = getHistoryVO( baseDTO);
        
        return history;
	}
	/**
	 * 行情订阅信息
	 */
	public RankingVO findWsTickerByCondition(String symbol) {
        // 交易行情
        RankingVO ranking = getRankingVO( symbol);
        return ranking;
	}
	/**
	 * 用户资产
	 */
	public Map<String, CapitalVO> findWsCapitalByCondition(String symbol, String userId) {
        Map<String, CapitalVO> capital = null;
        // 查询对象
        BaseDTO baseDTO = BaseDTO.builder().build();
        baseDTO.setSymbol( symbol);
        // 不为空 获取 订单数据
        if ( StrUtil.isNotEmpty( userId)) {
            baseDTO.setUserId(Integer.parseInt(userId));
            baseDTO.setState( BizConst.BIZ_STATUS_VALID);
            baseDTO.setArray( Arrays.asList( StrUtil.splitToArray( symbol, StrUtil.C_UNDERLINE)));
            // 我的资产数据
            capital = getCapitalAllVO( baseDTO);
            
        }
        return capital;
	}
	/**
	 * 委托订阅
	 */
	public PageVO findWsActivesByCondition(String symbol, String userId) {

		PageDTO pageDTO = PageDTO.builder()
                .symbol( symbol)
                .pageNum( AppConst.DEFAULT_PAGE_NO)
                .pageSize( AppConst.DEFAULT_LIMIT_SIZE ) // app 端只显示 五条数据
                .userId(Integer.parseInt(userId)).build();
		Page<OrderVO> info = PageHelper.startPage( pageDTO.getPageNum(), pageDTO.getPageSize(), Boolean.TRUE);
		List<OrderVO> list = orderMapper.queryUserDelegate( pageDTO);
		// 委托订单
        PageVO actives = PageVO.builder().list( list).total( info.getTotal()).build();
        
        return actives;
	}
	
	

    @Override
    public WsChatVO findUserChat(Integer orderId, Integer advertId, String token) {
        //获取用户信息
        UserDTO user = getLoginUser( token);
        //查询参数
        PageDTO dto = PageDTO.builder()
                            .userId(ObjectUtil.isEmpty( user)? null:user.getId())
                            .orderId( IntUtils.isEmpty( orderId) ? null: orderId)
                            .advertId( IntUtils.isEmpty( advertId) ? null:advertId).build();
        //聊天记录(不分页)
        Page<OTCChatVO> page = PageHelper.startPage( AppConst.DEFAULT_PAGE_NO, AppConst.DEFAULT_PAGE_SIZE, Boolean.FALSE);
        //列表
        List<OTCChatVO> list = chatMapper.query( BaseDTO.builder().build());
        //返回信息
        WsChatVO vo = WsChatVO.builder()
                                .list( list).build();
        return vo;
    }

    @Override
    public Boolean addChat(WsChatDTO dto) {
        //copy属性
        OTCChatEntity entity = BeanUtil.toBean( dto, OTCChatEntity.class);
        //消息类型(文本,图片)
        //用户信息
        UserDTO user = getLoginUser( dto.getToken());
        //用户id
        entity.setUserId( ObjectUtil.isEmpty( user)?null:user.getId());
        return chatMapper.add( entity) > 0;
    }

    /**
     * 交易tick
     * @param symbol
     * @return
     */
    protected RankingVO getRankingVO(String symbol) {
        RankingVO vo = null;
        List<RankingVO> list = redisPlugin.hget( RedisConst.PLATFORM_RANKING, BizUtils.market( symbol));
        
        for (int idx = 0;idx < list.size(); idx ++) {
            RankingVO ranking = list.get( idx);

            if ( StrUtil.equals( BizUtils.token( symbol), ranking.getSymbol())) {
                vo = ranking;
            }
        }
        return vo;
    }
    
    /**
     * 获取 实际盘口数据
     * @param dto
     * @return
     */
    protected Map<String, List<OrderVO>> getHandicapVO(BaseDTO dto) {
    	String type = StrUtil.equals( dto.getType(), BizConst.TradeConst.TRADE_TYPE_INPUTS_EN) ? RedisConst.PLATFORM_HANDICAP_BUY : RedisConst.PLATFORM_HANDICAP_SELL;
    	Map<String, List<OrderVO>> vo = redisPlugin.hget(type, dto.getSymbol());
    	if ( MapUtil.isEmpty( vo)) {
            vo = MapUtil.newHashMap();
            List<WsOrderDTO> list = orderMapper.queryHandicap( dto);
            for ( int idx = 0;idx < list.size(); idx ++) {
                WsOrderDTO wsOrderDTO = list.get( idx);
                vo.put( StrUtil.toString( wsOrderDTO.getDigit()), wsOrderDTO.getList());
            }
        }
        return vo;
    }


    /**
     * 获取成交历史
     * @param dto
     * @return
     */
    protected List<DealVO> getHistoryVO(BaseDTO dto) {
        List<DealVO> vo = redisPlugin.hget(RedisConst.PLATFORM_HISTORY, dto.getSymbol());
        if ( CollUtil.isEmpty( vo)) {
            vo = orderMapper.queryDeals( dto);
        }
        return vo;
    }
    
    /**
     * 用户资产数据
     * @param dto
     * @return
     */
    protected Map<String, CapitalVO> getCapitalVO(BaseDTO dto) {
        Map<String, CapitalVO> vo = new HashMap<>();
        List<CapitalVO> list = walletService.getAssets( dto);
        for (int idx = 0;idx < list.size(); idx ++) {
            CapitalVO capital = list.get( idx);
            vo.put( capital.getToken(), capital);
        }
        return vo;
    }
    
    /**
     * 用户资产所有数据
     * @param dto
     * @return
     */
    protected Map<String, CapitalVO> getCapitalAllVO(BaseDTO dto) {
        Map<String, CapitalVO> vo = new HashMap<>();
        List<CapitalVO> list = walletService.getAssetsAll( dto);
        for (int idx = 0;idx < list.size(); idx ++) {
            CapitalVO capital = list.get( idx);
            vo.put( capital.getToken(), capital);
        }
        return vo;
    }
}
