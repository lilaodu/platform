package com.blockchain.platform.utils;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.blockchain.platform.config.RateCfg;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.pojo.vo.RateVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 汇率工具类
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-29 11:18 AM
 **/
@Component
public class RateUtils {

    /**
     * 汇率配置信息
     */
    @Resource
    private RateCfg rateCfg;

    /**
     * kucoin 获取汇率
     * @return
     */
    public  Map<String, List<RateVO>> kucoin(){
        //返回数据
        Map<String, List<RateVO>> vo = MapUtil.newHashMap();
        try {
            // 汇率配置
            Map<String, Object> config = rateCfg.getConfigure();
            //kucoin 汇率接口
            String kucoin = rateCfg.getKucoinApi();
            for (Map.Entry<String, String> entryCfg : rateCfg.getMarket().entrySet()) {
                //获取市场对应的汇率
                HttpResponse response = HttpRequest.get( StrUtil.concat( Boolean.FALSE, kucoin, entryCfg.getKey())).execute();
                //请求数据
                Map<String, Object> map = JSON.parseObject( response.body(), Map.class);
                    //解析后的数据
                Map<String, Object> data = (Map<String, Object>) map.get( BizConst.BIZ_MAS_DATA);
                //市场汇率返回值
                List<RateVO> list = CollUtil.newArrayList();
                for (Map.Entry<String, Object> entry : config.entrySet()) {
                    //汇率的具体值
                    RateVO rate = RateVO.builder()
                            .label( StrUtil.toString( entry.getValue()))
                            .last( NumberUtil.toBigDecimal( StrUtil.toString( data.get( entry.getKey()))))
                            .symbol( entry.getKey()).build();
                    list.add( rate);
                }
                vo.put( entryCfg.getValue(), list);
            }
        }catch (Exception ex){
            vo = null;
        }
        return vo;
    }

    /**
     * coinbase 获取汇率
     * @return
     */
    public  Map<String, List<RateVO>> coinbase(){
        //返回数据
        Map<String, List<RateVO>> vo = MapUtil.newHashMap();
        try {
            // 汇率配置
            Map<String, Object> config = rateCfg.getConfigure();
            //kucoin 汇率接口
            String coinbase = rateCfg.getCoinbaseApi();
            for (Map.Entry<String, String> entryCfg : rateCfg.getMarket().entrySet()) {
                //获取市场对应的汇率
                HttpResponse response = HttpRequest.get( StrUtil.concat( Boolean.FALSE, coinbase, entryCfg.getKey())).execute();
                //请求数据
                Map<String, Object> map = JSON.parseObject( response.body(), Map.class);
                //解析后的数据
                Map<String, Object> result = (Map<String, Object>) map.get( BizConst.BIZ_MAS_DATA);
                //真正的汇率值
                Map<String,Object> data = (Map<String,Object>) result.get( BizConst.BIZ_MAS_RATES);
                //市场汇率返回值
                List<RateVO> list = CollUtil.newArrayList();
                for (Map.Entry<String, Object> entry : config.entrySet()) {
                    //汇率的具体值
                    RateVO rate = RateVO.builder()
                            .label( StrUtil.toString( entry.getValue()))
                            .last( NumberUtil.toBigDecimal( StrUtil.toString( data.get( entry.getKey()))))
                            .symbol( entry.getKey()).build();
                    list.add( rate);
                }
                vo.put( entryCfg.getValue(), list);
            }
        }catch (Exception ex){
            vo = null;
        }
        return vo;
    }
}
