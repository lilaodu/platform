package com.blockchain.platform.service.impl;


import com.alibaba.fastjson.JSON;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.pojo.entity.MarketCoinEntity;
import com.blockchain.platform.pojo.vo.KLineInsertMessageVo;
import com.blockchain.platform.pojo.vo.KLinesVo;
import com.blockchain.platform.utils.KlineUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class KlineService {
    /**
     * 缓存对象
     */
    @Resource
    private RedisPlugin redisPlugin;
    public static Set<String> pairs=new HashSet<>();


    @Autowired
    InFluxDbService inFluxDbService;
    public final Map<String, KlineUtil> klineUtils = new ConcurrentHashMap<>();

    @Autowired
    KlineService klineService;

    public void startKlineService(String pair) {
        pairs.add(pair);
        klineService.klineUtils.put(pair,new KlineUtil(pair));
    }

    public BigDecimal getMarketPrice(String pair){
        KlineUtil klineUtil = klineUtils.get(pair);
        if (klineUtil == null) return BigDecimal.ZERO;
      return klineUtil.currentMarketPrice;
    }


    public List<Number> getTendency(String code) {
        return inFluxDbService.getTendency(code);
    }


        public void doKlineInsert(KLineInsertMessageVo msg) {
        KlineUtil klineUtil = klineUtils.get(msg.getPair());
            KLinesVo isNeedSave = klineUtil.put(msg.getPrice(), msg.getNumber(), msg.getTimestamp());
//            System.out.println("插入k线  "+JSON.toJSONString(msg));

            if (isNeedSave != null){
            inFluxDbService.insertValue(msg.getPair(),isNeedSave);
        }
    }

    private static long getTypeStartTime(long time,long typeTime){
        return time - (time % typeTime);
    }

    private static long getTypeEndTime(long time,long typeTime){
        long l = time % typeTime;
        if (l == 0) return time;
        return time + (typeTime - l);
    }

    public List<KLinesVo> kline(String pair, Long start, Long end, Integer limit, String type) {
        if (limit == null || limit > 2000) limit = 2000;

        if (end == null || end ==0) end = System.currentTimeMillis();
        if (start == null || start ==0) start = end - limit * KlineUtil.klineTypes.get(type);

        start=(start/1000)*1000;
        end=(end/1000)*1000;

        List<KLinesVo> kline = inFluxDbService.queryKline(pair, start, end, type, limit);
        /*
         * 坑爹啊,卧槽,缓存数据,命中缓存取出来的数据是反向的
         */
        kline = new ArrayList<>(kline);
        KLinesVo lastKline = getLastKline(pair, type);
        if (kline.size() > 0){
            KLinesVo numbers = kline.get(0);
            long perTime = numbers.getTime();
            BigDecimal close = numbers.getClosePrice();
            Long s = KlineUtil.klineTypes.get(type);
            // 内存时间是在时间内的
            if (lastKline!=null&&lastKline.getTime() >= start && lastKline.getTime() <= end){
                // 内存时间 != k 线最新时间
                if (lastKline.getTime() != perTime ){
                    while (perTime + s < lastKline.getTime()){
                        perTime = perTime + s;
                        kline.add(new KLinesVo(type,close,close,close,close,perTime));
                    }
                    kline.add(0,lastKline);
                } else {
                    // 删除 k 先最新,补充成内存
                    kline.remove(0);
                    kline.add(0,lastKline);
                }
            }
        } else {
            if (lastKline!=null&&lastKline.getTime() > start && lastKline.getTime() < end){
                kline.add(0,lastKline);
            }
        }
        kline.sort(Comparator.comparingLong(o -> (long) o.getTime()));
        return kline;
    }

    public KLinesVo getLastKline(String pair, String klineType){
        KlineUtil klineUtil = klineUtils.get(pair);
        if (klineUtil == null) return null;
        KLinesVo kline = klineUtil.KLines.get(klineType);
        return kline;
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void task(){
        Map<String, MarketCoinEntity> marketCoinEntityMap =redisPlugin.get(RedisConst.PLATFORM_PAIRS_CONFIG);

        for(String key:marketCoinEntityMap.keySet()){
            MarketCoinEntity pairs=marketCoinEntityMap.get(key);
            final String pairName=pairs.selectPairName();
            BigDecimal marketPrice = this.getMarketPrice(pairName);
            if (marketPrice.compareTo(BigDecimal.ZERO) != 0){
                this.doKlineInsert(new KLineInsertMessageVo(pairName,marketPrice,BigDecimal.ZERO,System.currentTimeMillis()-5000));
            }

        }

    }

}
