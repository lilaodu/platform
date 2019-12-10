package com.blockchain.platform.dictionary;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blockchain.platform.config.HuobiApiCfg;
import com.blockchain.platform.config.RateCfg;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.QueryConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.disruptor.LinkedBlockTrade;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.entity.*;
import com.blockchain.platform.pojo.vo.*;
import com.blockchain.platform.service.IDictionaryService;
import com.blockchain.platform.service.IHuobiKLineService;
import com.blockchain.platform.service.ILockWarehouseConfigService;
import com.blockchain.platform.service.impl.KlineService;
import com.blockchain.platform.utils.BigDecimalUtils;
import com.blockchain.platform.utils.BizUtils;
import com.blockchain.platform.utils.IntUtils;
import com.blockchain.platform.utils.RateUtils;
import com.blockchain.platform.websocket.NettyWebSocket;
import com.huobi.client.SubscriptionClient;
import com.huobi.client.model.enums.CandlestickInterval;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.nio.cs.ext.MacArabic;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据字典工厂类
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-14 11:30 AM
 **/
@Component
public class DictionaryFactory {

    public  static Map<String,LinkedBlockTrade> blockTradeMap = new ConcurrentHashMap<>();


      public static  volatile Map<String,Map<String,Object>>memoryDic=new ConcurrentHashMap<>(100);




      public static  void putMemoryDic(String key,String item,Object value){
          Map<String,Object>objectMap= memoryDic.getOrDefault(key,new ConcurrentHashMap<>());
          objectMap.put(item,value);
          memoryDic.put(key,objectMap);
      }


      public static Object getMemoryDic(String key,String item)
      {
          Map<String,Object>objectMap= memoryDic.getOrDefault(key,null);
          if(objectMap==null) return null;
          return objectMap.getOrDefault(item,null);

      }

    /**
     * 缓存对象
     */
    @Resource
    private RedisPlugin redisPlugin;

    /**
     * 汇率配置
     */
    @Resource
    private RateCfg rateCfg;

    /**
     * 汇率工具类
     */
    @Resource
    private RateUtils rateUtils;

    /**
     * 字典类接口
     */
    @Resource
    private IDictionaryService dictService;
    
    @Resource
    private ILockWarehouseConfigService  LockWarehouseConfigService;
    
    @Resource
    private IHuobiKLineService huobiKLineService;

    /**
     * 转换
     */
    @Resource
    private HuobiApiCfg huobiApiCfg;
    

    /**
     * 首页banner
     */
    public void banner() {
        List<BannerVO> list = dictService.banner( BaseDTO.builder()
                                                .state( BizConst.BIZ_STATUS_VALID)
                                                .limit( AppConst.DEFAULT_LIMIT_SIZE).build());
        // banner 信息
        redisPlugin.set( RedisConst.PLATFORM_BANNER, list);
    }

    /**
     * 首页通知
     */
    public void notice() {
        List<NoticeVO> list = dictService.notice( BaseDTO.builder()
                                                .state( BizConst.BIZ_STATUS_VALID).build());
        // 活动通知 信息
        redisPlugin.set( RedisConst.PLATFORM_NOTICE, list);
    }

    /**
     * 文章列表
     */
    public void article() {
        // 默认查询上架的文章
        List<ArticleVO> list = dictService.article( BaseDTO.builder()
                                                .state( BizConst.ArticleConst.STATE_PUBLISH).build());

        // 分类放置 数据
        Map<String, List<ArticleVO>> holder = MapUtil.newHashMap();
        for ( int idx = 0;idx < list.size(); idx ++) {
            ArticleVO vo = list.get( idx);
            vo.setContent( StrUtil.EMPTY);
            // 当前对象
            List<ArticleVO> current = null;
            //
            if( holder.containsKey( vo.getCategory())) {
                current = holder.get( vo.getCategory());
            } else {
                current = new ArrayList<>();
            }
            current.add( vo);
            holder.put( vo.getCategory(), current);
        }
        // 存入缓存
        if (CollUtil.isNotEmpty( list)) {
            for (Map.Entry<String, List<ArticleVO>> entry : holder.entrySet()) {
                redisPlugin.hset(RedisConst.PLATFORM_ARTICLE, entry.getKey(), entry.getValue());
            }
        } else {
            redisPlugin.del( RedisConst.PLATFORM_ARTICLE);
        }
    }

    /**
     * 抽奖活动
     */
    public void draw(){
        //查询抽奖活动
        List<LuckDrawVO> list = dictService.draw( BaseDTO.builder()
                                        .state( BizConst.BIZ_STATUS_VALID).build());
        //遍历活动
        if( CollUtil.isNotEmpty( list)){
            for( int idx = 0; idx < list.size(); idx ++){
                //奖项
                List<LuckDrawConfigVO> prize = list.get( idx).getPrize();
                //概率
                Map<String,LuckDrawConfigVO> pro = MapUtil.newHashMap();
                for( int index = 0; index < prize.size() ; index ++){
                    //奖项
                    LuckDrawConfigVO vo = prize.get( index);
                    pro.put( StrUtil.toString(vo.getPrize()), vo);
                }
                redisPlugin.hset( RedisConst.DRAW_ACTIVITY, StrUtil.toString( list.get( idx).getId()), pro);
            }
        }
        //页面获取
        redisPlugin.set( RedisConst.DRAW_LIST, list);
    }
    
    /**
     * 初始化 交易 排行榜
     */
    public void ranking() {
        try {
            // 实际tick
            List<RankingVO> list = dictService.ranking( BaseDTO.builder().state( BizConst.BIZ_STATUS_VALID).build());

            //交易市场行情
            Map<String, List<RankingVO>> map = MapUtil.newHashMap();
            //遍历
            for (int idx = 0;idx < list.size() ;idx ++ ){
                RankingVO vo = list.get( idx);
                List<RankingVO> current = null;
                if( map.containsKey( vo.getMarket())) {
                    current = map.get( vo.getMarket());
                } else {
                    current = new ArrayList<>();
                }
                current.add( vo);
                map.put( vo.getMarket(), current);
            }
            //首页排行榜（涨 跌 新币）
            Map<String, List<RankingVO>> home = MapUtil.newHashMap();


            //涨
            home.put( BizConst.Ranking.RANKING_RISE, BizUtils.sort( BizConst.Ranking.RANKING_RISE, list));
            //跌
            home.put( BizConst.Ranking.RANKING_FALL, BizUtils.sort( BizConst.Ranking.RANKING_FALL, list));
            //新币
            home.put( BizConst.Ranking.RANKING_NEW, BizUtils.sort( BizConst.Ranking.RANKING_NEW, list));
            //存储首页的redis
            redisPlugin.set( RedisConst.PLATFORM_HOME_RANKING, home);
            // 存储redis
            for (Map.Entry<String, List<RankingVO>> entry : map.entrySet()) {
                redisPlugin.hset( RedisConst.PLATFORM_RANKING, entry.getKey(), entry.getValue());
            }
        }catch (Exception ex){
            //不处理
            ex.printStackTrace();
        }
    }

    /**
     * 市场配置
     */
    public void market() {
        //查询有效的货币配置
    	List<MarketConfigEntity> list = dictService.market( BaseDTO.builder()
                                                .state( BizConst.BIZ_STATUS_VALID).build());
    	
        redisPlugin.set(RedisConst.MARKET, list);
    }
    
    /**
     * 货币配置
     */
    public void coin() {
        //查询有效的货币配置
        List<CoinEntity> list = dictService.coin( BaseDTO.builder().state( BizConst.BIZ_STATUS_VALID).build());
        Map<String, CoinEntity> holder = MapUtil.newHashMap();

        // 货币API接口
        String domain = huobiApiCfg.getHuobiTickerApi();

        for (int idx = 0;idx < list.size();idx ++) {
            CoinEntity entity = list.get( idx);
            // 获取货币收盘价
            if ( !StrUtil.equals( entity.getSymbol(), BizConst.BASE_TOKEN)){
                try {
                    // 获取默认数据
                    String response = HttpUtil.get( StrUtil.concat( Boolean.FALSE, domain,
                            entity.getSymbol().toLowerCase(),
                            BizConst.USDT_TOKEN.toLowerCase()));

                    HuobiTickerVO tickerVO = JSONUtil.toBean( response, HuobiTickerVO.class);

                    // 市值
                    entity.setMarketPrice( BigDecimalUtils.toBigDecimal(tickerVO.getTick().getClose()));
                } catch (Exception ex) {
                    // 不处理
                }
            }
            holder.put( entity.getSymbol(), entity);
        }
        redisPlugin.set(RedisConst.PLATFORM_COIN_CONFIG, holder);
    }


    @Autowired
    KlineService klineService;
    /**
     * 交易对配置
     */
    public void pairs(){
        //获取有效的交易对
        List<MarketCoinEntity> list = dictService.pairs( BaseDTO.builder().state( BizConst.BIZ_STATUS_VALID).build());
        Map<String, MarketCoinEntity> holder = MapUtil.newHashMap();
        for (int idx = 0;idx < list.size(); idx ++) {
            MarketCoinEntity entity = list.get( idx);
            holder.put(entity.getTableName(), entity);
            klineService.startKlineService(entity.selectPairName());


        }
        redisPlugin.set(RedisConst.PLATFORM_PAIRS_CONFIG, holder);
    }
    
    /**
     * 秒合约配置
     * @param isAgain 是否重新获取
     */
    public void secondsContract(Boolean isAgain){
    	List<SecondsContractEntity> list = null;

        if( !isAgain){
            list = redisPlugin.get(RedisConst.SECONDS_CONTRACT_CONFIG);
        } else {
            list = dictService.secondsContract( BaseDTO.builder()
                    .state( BizConst.BIZ_STATUS_VALID).build());
            // 配置信息
            redisPlugin.set(RedisConst.SECONDS_CONTRACT_CONFIG, list);
        }

    	// 排行信息
    	List<HuobiKLineEntity> ranking = CollUtil.newArrayList();

    	for ( SecondsContractEntity entity : list) {

            String action = huobiApiCfg.getHuobiHistoryApi();

            String symbol = StrUtil.replace( entity.getCoinPair(), StrUtil.UNDERLINE, StrUtil.EMPTY).toLowerCase();

            action = StrUtil.concat(Boolean.FALSE,  action, symbol);

            String response = HttpUtil.get( action);

            HuobiTickerVO huobiTickerVO = JSONUtil.toBean( response, HuobiTickerVO.class);

            HuobiKLineEntity huobiKLineEntity = huobiTickerVO.getTick();
            if(huobiKLineEntity != null) {
                huobiKLineEntity.setSymbol(entity.getCoinPair());
                huobiKLineEntity.setVol(huobiKLineEntity.getVolume());
                huobiKLineEntity.setOp(huobiKLineEntity.getOpen());
                huobiKLineEntity.setCl(huobiKLineEntity.getClose());
                ranking.add(huobiKLineEntity);
            }
        }
        // 排行榜
        redisPlugin.set(RedisConst.SECONDS_CONTRACT_RANKING, ranking);
    }

    /**
     * 参数信息
     */
    public void params() {
        //获取有效的参数信息
        List<ParamsVO> list = dictService.params(BaseDTO.builder()
                                                    .state( BizConst.BIZ_STATUS_VALID).build());
        for (ParamsVO param: list) {
            redisPlugin.hset(RedisConst.PLATFORM_PARAMS, param.getParamType(), param.getParamValue() );
        }

    }

    /**
     * 交易汇率
     */
    public Map<String, List<RateVO>> rates() {
        //返回数据
        Map<String, List<RateVO>> vo = MapUtil.newHashMap();
        try {
            //调用汇率工具获取汇率值
            vo = rateUtils.coinbase();
            if( ObjectUtil.isEmpty( vo)){
                //调用 coinbase 获取汇率值
                vo = rateUtils.coinbase();
            }
		} catch (Exception e) {
			vo = null;
		}
        // 更新缓存
        redisPlugin.set( RedisConst.PLATFORM_RATE_CONFIG, vo);
        //返回数据
        return vo;
    }

    /**
     * 汇率
     * @return
     */
    public List<String> currency(){
        //返回数据
        List<String> currency = CollUtil.newArrayList();
        // 汇率配置
        Map<String, Object> config = rateCfg.getConfigure();
        if(ObjectUtil.isNotEmpty( config)){
            for(Map.Entry<String, Object> entry : config.entrySet()){
                currency.add( entry.getKey());
            }
        }
        //更新缓存
        redisPlugin.set( RedisConst.PLATFORM_CURRENCY_CONFIG, currency);
        return currency;
    }

    /**
     * 锁仓配置
     */
    public void lockWarehouseContract(){
    	
    	QueryWrapper<LockWarehouseConfigEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(BizConst.sql.STATE, BizConst.BIZ_STATUS_VALID);
    	LockWarehouseConfigEntity entity = LockWarehouseConfigService.getOne(queryWrapper);
        redisPlugin.set(RedisConst.LOCK_WAREHOUSE_CONFIG, entity);
    }
    
    /**
     * 开启火币请,求获取秒合约对应市场的k线数据
     */
    public void huoBiWS(){
    	
    	SubscriptionClient subscriptionClient = SubscriptionClient.create();
    	List<SecondsContractEntity> list = dictService.secondsContract( BaseDTO.builder().state( BizConst.BIZ_STATUS_VALID).build());

    	for(SecondsContractEntity contract : list) {
    		String var = StrUtil.replace(contract.getCoinPair(), StrUtil.UNDERLINE, StrUtil.EMPTY).toLowerCase();

            HuobiKLineEntity kline = HuobiKLineEntity.builder().build();
            subscriptionClient.subscribeCandlestickEvent(var, CandlestickInterval.MIN1, (candlestickEvent) -> {
                kline.setCl(candlestickEvent.getData().getClose());
                kline.setHigh(candlestickEvent.getData().getHigh());
                kline.setLow(candlestickEvent.getData().getLow());
                kline.setOp(candlestickEvent.getData().getOpen());
                kline.setSymbol(contract.getCoinPair());
                kline.setOpen(candlestickEvent.getData().getOpen());
                kline.setDate(candlestickEvent.getData().getTimestamp());
                kline.setClose(candlestickEvent.getData().getClose());
                kline.setVolume(candlestickEvent.getData().getVolume());
                kline.setCount(candlestickEvent.getData().getCount());
                kline.setAmount(candlestickEvent.getData().getAmount());
                kline.setInterv(candlestickEvent.getInterval().name());

                if (!BeanUtil.isEmpty(kline)) {
                    DictionaryFactory.putMemoryDic(RedisConst.HUOBI_WS_KLINE, contract.getCoinPair(), kline);
                }

            });
        }
    }


    public HuobiKLineEntity getLastHuoBiKline(String pairName)
    {

        return (HuobiKLineEntity) DictionaryFactory.getMemoryDic(RedisConst.HUOBI_WS_KLINE, pairName);
    }

    /**
     * 定时将火币K线数据存入数据库
     */
    public void huoBiDataToSQl(){
    	List<SecondsContractEntity> list = redisPlugin.get(RedisConst.SECONDS_CONTRACT_CONFIG);
    	for(SecondsContractEntity contract : list) {
    		String var = StrUtil.replace(contract.getCoinPair(), StrUtil.UNDERLINE, StrUtil.EMPTY).toLowerCase();
    		List<HuobiKLineEntity> klist = redisPlugin.hget(RedisConst.HUOBI_WS_DATA, var);
    		if ( CollUtil.isNotEmpty( klist) && klist.size() > 2) {
    		    List value = CollUtil.sub( klist, 0, klist.size() - 1);

                if(huobiKLineService.saveBatch( value)) {

                    List<HuobiKLineEntity> surplus = CollUtil.newArrayList( klist.get( klist.size() - 1));

                    redisPlugin.hset(RedisConst.HUOBI_WS_DATA, var, surplus);
                }
            }
    	}
    	
    	
    }

    /**
     * 更新redis中的广告列表
     */
    public void advert(){
        //存进缓存，方便取
        Map<String, OTCAdvertVO> map = MapUtil.newHashMap();

        //获取广告列表
        List<OTCAdvertVO> list = dictService.advert( BaseDTO.builder()
                                .state( BizConst.BIZ_STATUS_VALID).build());
        //封装数据
        for(int idx = 0; idx < list.size(); idx ++){
            OTCAdvertVO vo = list.get( idx);
            map.put( vo.getAdvertNumber(), vo);
        }


        List<String> merchants = dictService.merchants( BaseDTO.builder().state( BizConst.MerchantConst.STATE_COMPLETE).build());
        //存君缓存
        redisPlugin.set( RedisConst.PLATFORM_OTC_ADVERT, map);

        redisPlugin.set( RedisConst.PLATFORM_OTC_MERCHANTS, merchants);
    }

    /**
     * 钱包配置
     */
    public void wallet(){
        //存进缓存
        Map<String, WalletConfigEntity> map = MapUtil.newHashMap();
        //获取配置列表
        List<WalletConfigEntity> list = dictService.wallet( BaseDTO.builder()
                                        .state( BizConst.BIZ_STATUS_VALID).build());
        //封装数据
        for(int idx = 0; idx < list.size(); idx ++){
            WalletConfigEntity entity = list.get( idx);
            map.put( entity.getSymbol(), entity);
        }
        //存缓存
        redisPlugin.set( RedisConst.PLATFORM_WALLET_CONFIG, map);
    }


    /**
     * 升级配置
     */
    public void upgrade(){
        List<UserUpgradeEntity> list = dictService.upgrade();
        Map<String, Map<Integer, UserUpgradeEntity>> config = MapUtil.newHashMap();

        for (UserUpgradeEntity entity : list) {

            Map<Integer, UserUpgradeEntity> map = null;

            if( config.containsKey( entity.getType())) {
                map = config.get( entity.getType());
            } else {
                map = MapUtil.newHashMap();
            }
            map.put( entity.getSn(), entity);

            config.put( entity.getType(), map);
        }
        if ( MapUtil.isNotEmpty( config)) {
            for (Map.Entry<String, Map<Integer, UserUpgradeEntity>> entry : config.entrySet()) {
                redisPlugin.hset(RedisConst.PLATFORM_UPGRADE_CONFIG, entry.getKey(), entry.getValue());
            }
        }
    }
    public void startMatchThread() {
        List<MarketCoinEntity> list = dictService.pairs( BaseDTO.builder().state( BizConst.BIZ_STATUS_VALID).build());
        for (int idx = 0;idx < list.size(); idx ++) {
            MarketCoinEntity entity = list.get( idx);

            LinkedBlockTrade linkedBlockTrade = new LinkedBlockTrade();
            linkedBlockTrade.pairName = entity.selectPairName();
            linkedBlockTrade.symbol = entity.selectPairName();
            linkedBlockTrade.init();

            blockTradeMap.put(entity.getTableName(), linkedBlockTrade);
        }
        System.out.println("撮合启动成功");
    }
}
