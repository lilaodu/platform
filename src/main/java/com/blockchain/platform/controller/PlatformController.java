package com.blockchain.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.dictionary.DictionaryFactory;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.LimitDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.entity.*;
import com.blockchain.platform.pojo.vo.*;
import com.blockchain.platform.service.IDictionaryService;
import com.blockchain.platform.service.IHuobiKLineService;
import com.blockchain.platform.utils.ExUtils;
import com.blockchain.platform.utils.IntUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * 主页控制器
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-15 1:05 PM
 **/
@RestController
@RequestMapping(StrUtil.EMPTY)
public class PlatformController extends BaseController {

	
	
	/**
     * 数据字典工厂构造
     */
    @Resource
    private DictionaryFactory factory;

    /**
     * 字典服务接口
     */
    @Resource
    private IDictionaryService dictionaryService;


    /**
     * 货币KLine 数据
     */
    @Resource
    private IHuobiKLineService huobiKLineService;
    
	
    /**
     * 平台banner 查询
     * @param dto
     * @return
     */
    @RequestMapping("/banner")
    public ResponseData banner(@RequestBody LimitDTO dto) {
        ResponseData data = ResponseData.ok();
        try {
            //获取缓存的banner信息
            List<BannerVO> vo = redisPlugin.get( RedisConst.PLATFORM_BANNER);
            //判断是否限制条数
            if( IntUtils.isEmpty( dto.getLimit())) {
                data.setData( vo);
            } else {
                data.setData( CollUtil.sub( vo, getLimitBegin(),  dto.getLimit()));
            }
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 通知消息
     * @param dto
     * @return
     */
    @RequestMapping("/notice")
    public ResponseData notice(@RequestBody LimitDTO dto){
        ResponseData data = ResponseData.ok();
        try {
            //获取缓存的通知消息
            List<NoticeVO> vo = redisPlugin.get(RedisConst.PLATFORM_NOTICE);
            //返回页面
            data.setData( vo);
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 文章列表 区域
     * @param dto
     * @return
     */
    @RequestMapping("/article")
    public ResponseData article(@RequestBody PageDTO dto){
        ResponseData data = ResponseData.ok();
        try {
            // 当前类型的文章
            List<ArticleVO> vos = redisPlugin.hget( RedisConst.PLATFORM_ARTICLE, dto.getType());
            // 返回页面数据
            List<ArticleVO> list = new ArrayList<>();
            if(CollUtil.isNotEmpty(vos)){
                /**
                 * 逻辑分页
                 */
                //总条数
                int offset = dto.getPageNum() * dto.getPageSize();
                //开始记录
                int start = (dto.getPageNum() - 1) * dto.getPageSize();
                //判断是否查询全部
                if ( IntUtils.compareTo( offset, vos.size())) {
                    list.addAll( vos);
                } else {
                    for ( int idx = start;idx < offset; idx ++) {
                        if ( idx < vos.size()) {
                            list.add( vos.get( idx));
                        }
                    }
                }
            }
            data.setData( PageVO.builder()
                                    .total( CollUtil.isNotEmpty( vos) ?
                                        IntUtils.toLong( vos.size()) : IntUtils.toLong( IntUtils.INT_ZERO))
                                    .list( list).build());
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 系统级货币配置
     * @param dto
     * @return
     */
    @RequestMapping("/coin")
    public ResponseData coin(@RequestBody LimitDTO dto){
        ResponseData data = ResponseData.ok();
        try {
            // 获取缓存货币数据
            Map<String, CoinEntity> map = redisPlugin.get( RedisConst.PLATFORM_COIN_CONFIG);
            if ( MapUtil.isNotEmpty( map)) {
                Map<String, CoinVO> vo = MapUtil.newHashMap();
                //遍历map
                for (Map.Entry<String, CoinEntity> entry : map.entrySet()) {
                    vo.put( entry.getKey(), BeanUtil.toBean( entry.getValue(), CoinVO.class));
                }
                //返回数据
                data.setData( vo);
            }
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * app下载
     * @return
     */
    @RequestMapping("/version")
    public ResponseData version(){
        ResponseData data = ResponseData.ok();
        try {
            //设置 版本号以及下载地址
            VersionVO vo = VersionVO.builder()
                                .iosVersion( redisPlugin.hget( RedisConst.PLATFORM_PARAMS, RedisConst.IOS_VERSION))
                                .iosAddress( redisPlugin.hget( RedisConst.PLATFORM_PARAMS, RedisConst.IOS_DOWN))
                                .androidVersion( redisPlugin.hget( RedisConst.PLATFORM_PARAMS, RedisConst.ANDROID_VERSION))
                                .androidAddress( redisPlugin.hget( RedisConst.PLATFORM_PARAMS, RedisConst.ANDROID_DOWN)).build();
            //返回数据
            data.setData( vo);
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 当前系统交易对
     * @return
     */
    @RequestMapping("/pairs")
    public ResponseData pairs(){
        ResponseData data = ResponseData.ok();
        try {
            //获取缓存中交易对信息
            Map<String, MarketCoinEntity> map = redisPlugin.get( RedisConst.PLATFORM_PAIRS_CONFIG);
            //页面返回数据
            List<String> values = CollUtil.newArrayList();
            if ( MapUtil.isNotEmpty( map)) {
                //遍历map
                for (Map.Entry<String, MarketCoinEntity> entry : map.entrySet()) {
                    //处理格式
                    values.add( StrUtil.replace( entry.getKey(), StrUtil.UNDERLINE, StrUtil.SLASH));
                }
            }
            data.setData( values);
            
        } catch (Exception ex) {
            ExUtils.error(ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 汇率配置
     * @return
     */
    @RequestMapping("/rate")
    public ResponseData rate(){
        ResponseData data = ResponseData.ok();
        try {
            Map<String, List<RateVO>> vo = redisPlugin.get( RedisConst.PLATFORM_RATE_CONFIG);
            if( MapUtil.isNotEmpty( vo)){
                data.setData( vo);
            }
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }
    
    /**
     * 刷新
     * @return
     */
    @RequestMapping("/flush")
    public ResponseData flush(){
        ResponseData data = ResponseData.ok();
        try {
            // 首页banner
            factory.banner();
            // 通知信息
            factory.notice();
            // 默认文章
            factory.article();
            // 货币配置
            factory.coin();
            // 交易对信息
            factory.pairs();
            // 秒合约信息
            factory.secondsContract(Boolean.TRUE);
            // 参数信息
            factory.params();
            // 排行榜
            factory.ranking();
            // 汇率
            factory.rates();
            // 所有市场
            factory.market();
            // 锁仓配置
            factory.lockWarehouseContract();
            // 抽奖活动
            factory.draw();
            //
            factory.currency();
            //广告信息
            factory.advert();
            //钱包配置
            factory.wallet();
        } catch (Exception ex){
            ExUtils.error(ex, LocaleKey.SYS_QUERY_FAILED);
        }
        
        return data;
    }
    

    /**
     * 获取所有市场
     * @return
     */
    @RequestMapping("/market")
    public ResponseData market(){
        ResponseData data = ResponseData.ok();
        try {
            //返回数据
            List<String>  list = CollUtil.newArrayList();
            //所有市场
            List<MarketConfigEntity> markets = redisPlugin.get(RedisConst.MARKET);
            for(int idx = 0; idx < markets.size(); idx ++){
                list.add( markets.get( idx).getMarket());
            }
            data.setData( list);
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 汇率值(DictionaryRunner中注释了调用)
     * @return
     */
    @RequestMapping("/rates")
    public ResponseData rates(){
        ResponseData data = ResponseData.ok();
        try {
            //获取对应汇率的数据
            Map<String, List<RateVO>> vo = redisPlugin.get( RedisConst.PLATFORM_RATE_CONFIG);
            //解析数据
            data.setData( vo);
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 法币
     * @return
     */
    @RequestMapping("/currency")
    public ResponseData currency(){
        ResponseData data = ResponseData.ok();
        try {
            //获取法币
            List<String> list = redisPlugin.get( RedisConst.PLATFORM_CURRENCY_CONFIG);
            //返回
            data.setData( list);
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 获取字典信息
     * @return
     */
    @RequestMapping("/dict")
    public ResponseData dict(@RequestBody BaseDTO dto){
        ResponseData data = ResponseData.ok();
        try {
            //有效的字段值
            dto.setState( BizConst.BIZ_STATUS_VALID);
            //执行查询
            List<String> list = dictionaryService.dict( dto);
            //列表
            data.setData( list);
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 抽奖信息
     * @param dto
     * @return
     */
    @RequestMapping("/draw")
    public ResponseData draw(@RequestBody LimitDTO dto) {
        ResponseData data = ResponseData.ok();
        try {
            List<LuckDrawVO> list = redisPlugin.get( RedisConst.DRAW_LIST);
            //移除奖项
            for(int idx = 0 ; idx < list.size(); idx ++){
                list.get( idx).setPrize(null);
            }
            //判断是否限制了展示条数
            if( IntUtils.isEmpty( dto.getLimit())) {
                data.setData( list);
            } else {
                data.setData( CollUtil.sub( list, getLimitBegin(),  dto.getLimit()));
            }
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 抽奖信息奖项
     * @param id
     * @return
     */
    @RequestMapping("/draw/{id}")
    public ResponseData prize(@PathVariable("id") Integer id) {
        ResponseData data = ResponseData.ok();
        try {
            List<LuckDrawVO> list = redisPlugin.get( RedisConst.DRAW_LIST);
            //获取奖项
            for(int idx = 0; idx < list.size(); idx ++){
                if( IntUtils.equals( id, list.get( idx).getId())){
                    LuckDrawVO vo = list.get( idx);
                    for(LuckDrawConfigVO configVO : vo.getPrize()) {
                        configVO.setProbability( null);
                    }
                    data.setData( vo);
                }
            }
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 秒合约 行情
     * 根据配置 是否显示为首页
     * 走 redis
     * @param dto
     * @return
     */
    @RequestMapping("/second")
    public ResponseData second(@RequestBody LimitDTO dto){
        ResponseData data = ResponseData.ok();
        try {

            // 获取配置
            List<HuobiKLineEntity> array = redisPlugin.get( RedisConst.SECONDS_CONTRACT_RANKING);

            if ( CollUtil.isEmpty( array)) {

                List<SecondsContractEntity> list = redisPlugin.get(RedisConst.SECONDS_CONTRACT_CONFIG);

                for (SecondsContractEntity entity : list) {
                    String var = StrUtil.replace( entity.getCoinPair(), StrUtil.UNDERLINE, StrUtil.EMPTY).toLowerCase();
                    
                    List<Object> Objects = redisPlugin.hget(RedisConst.HUOBI_WS_DATA, var);

                    HuobiKLineEntity huobiKLineEntity = BeanUtil.toBean(Objects.get( Objects.size() - 1), HuobiKLineEntity.class);
                    if( StrUtil.isEmpty( huobiKLineEntity.getSymbol()) ) {
                        QueryWrapper<HuobiKLineEntity> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq(BizConst.sql.SYMBOL, entity.getCoinPair()).orderByDesc(BizConst.sql.ID).last("LIMIT 1");
                        huobiKLineEntity = huobiKLineService.getOne(queryWrapper);
                    }
                    array.add( huobiKLineEntity);
                }
            }
            data.setData( array);
        } catch (Exception ex) {
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

    /**
     * 广告配置
     * @return
     */
    @RequestMapping("/advert/config")
    public ResponseData advert(){
        ResponseData data = ResponseData.ok();
        try {
            //获取广告配置
            String config = redisPlugin.hget( RedisConst.PLATFORM_PARAMS, BizConst.ParamsConst.ADVERT_CONFIG);
            //解析数据
            Map<String,String> map =  JSON.parseObject( config, new TypeReference<Map<String, String>>(){});
            //返回数据
            data.setData( map);
        }catch (Exception ex){
            ExUtils.error( ex, LocaleKey.SYS_QUERY_FAILED);
        }
        return data;
    }

}
