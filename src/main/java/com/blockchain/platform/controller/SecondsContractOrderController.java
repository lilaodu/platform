package com.blockchain.platform.controller;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blockchain.platform.dictionary.DictionaryFactory;
import com.blockchain.platform.enums.Method;
import com.blockchain.platform.mapper.UserMapper;
import com.blockchain.platform.pojo.entity.*;
import com.blockchain.platform.service.IKycService;
import com.blockchain.platform.task.SecondsContractTast;
import com.squareup.moshi.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blockchain.platform.annotation.DuplicateVerify;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.BaseDTO;
import com.blockchain.platform.pojo.dto.CurrencyMarketTradeDTO;
import com.blockchain.platform.pojo.dto.PageDTO;
import com.blockchain.platform.pojo.dto.SecondsContractOrderDTO;
import com.blockchain.platform.pojo.dto.UserDTO;
import com.blockchain.platform.pojo.dto.UserWalletDTO;
import com.blockchain.platform.service.ISecondsContractOrderService;
import com.blockchain.platform.service.IUnlockWarehouseFlowService;
import com.blockchain.platform.service.IUserWalletService;
import com.blockchain.platform.utils.BizUtils;
import com.blockchain.platform.utils.ExUtils;
import com.blockchain.platform.utils.IntUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author zhangye
 */
@RestController
@RequestMapping("/secondsContract")
public class SecondsContractOrderController extends BaseController {

    @Autowired
    DictionaryFactory dictionaryFactory;

    /**
     * 钱包接口
     */
    @Resource
    private IUserWalletService userWalletService;

    @Resource
    private ISecondsContractOrderService secondsContractOrderService;

    @Resource
    private IUnlockWarehouseFlowService unlockWarehouseFlowService;

    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();


    @Autowired
    SecondsContractTast secondsContractTast;

    @Resource
    private IKycService kycService;

    /**
     * 秒合约配置
     *
     * @param coinPair
     * @return
     */
    @RequestMapping("/query")
    public ResponseData query(@RequestBody BaseDTO coinPair) {
        ResponseData data = ResponseData.ok();
        //redis获取配置信息
        List<SecondsContractEntity> lists = redisPlugin.get(RedisConst.SECONDS_CONTRACT_CONFIG);
        if (BeanUtil.isEmpty(lists)) {
            throw ExUtils.error(LocaleKey.SYS_QUERY_FAILED);
        }
        SecondsContractEntity dataVO = null;
        for (SecondsContractEntity list : lists) {
            if (StrUtil.equals(list.getCoinPair(), coinPair.getSymbol())) {
                dataVO = list;
                break;
            }
        }
        if (BeanUtil.isEmpty(dataVO)) {
            throw ExUtils.error(LocaleKey.SECONDS_CONTRACT_CONFIG_NULL);
        }
        data.setData(dataVO);
        return data;
    }

    /**
     * 进行秒合约的交易对
     *

     * @return
     */
    @RequestMapping("/secondsQuery")
    public ResponseData secondsQuery() {
        ResponseData data = ResponseData.ok();
        //redis获取配置信息
        List<SecondsContractEntity> lists = redisPlugin.get(RedisConst.SECONDS_CONTRACT_CONFIG);
        if (BeanUtil.isEmpty(lists)) {
            throw ExUtils.error(LocaleKey.SYS_QUERY_FAILED);
        }
        Map<String, List<String>> map = MapUtil.newHashMap();
        for (SecondsContractEntity list : lists) {
            String market = BizUtils.market(list.getCoinPair());
            if (map.get(market) == null) {
                map.put(market, CollUtil.newArrayList(BizUtils.token(list.getCoinPair())));
            } else {
                List<String> l = map.get(market);
                l.add(BizUtils.token(list.getCoinPair()));
                map.put(market, l);
            }
        }
        data.setData(map);
        return data;
    }

    @Resource
    UserMapper userMapper;


    /**
     * 用户押注
     */
    @RequestMapping("/stake")
    @DuplicateVerify(method = Method.SC_ORDER)
    public ResponseData stake(@RequestBody SecondsContractOrderDTO dto, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();
        /**
         * 1.获取secondsContractId对应的配置信息
         * 2.获取当前对应价格
         * 3.获取用户
         * 4.判断未开奖的订单数
         * 5.获取钱包信息对比price，是否可以押注
         * 6.扣款押注
         * 7.扣款通知
         * 8.生成秒合约订单
         * 9.开启计时器
         */
        List<SecondsContractEntity> list = redisPlugin.get(RedisConst.SECONDS_CONTRACT_CONFIG);
        SecondsContractEntity secondsContractEntity = null;
        //检测参数
        for (SecondsContractEntity entity : list) {
            if (IntUtils.equals(entity.getId(), dto.getSecondsContractId())) {
                secondsContractEntity = entity;
                break;
            }
        }
        if (BeanUtil.isEmpty(secondsContractEntity)) {
            throw ExUtils.error(LocaleKey.SYS_PARAM_ERROR);
        }
        String[] payCoin = StrUtil.split(secondsContractEntity.getSymbol(), "_");
        if (!ArrayUtil.contains(payCoin, dto.getPayCoin())) {
            throw ExUtils.error(LocaleKey.SYS_PARAM_ERROR);
        }
        if (StrUtil.equals(payCoin[0], dto.getPayCoin())) {
            if (!ArrayUtil.contains(StrUtil.split(secondsContractEntity.getCoinNums(), ","), dto.getPrice().toString())) {
                throw ExUtils.error(LocaleKey.SYS_PARAM_ERROR);
            }
        }
        if (StrUtil.equals(payCoin[1], dto.getPayCoin())) {
            if (!ArrayUtil.contains(StrUtil.split(secondsContractEntity.getMarketNums(), ","), dto.getPrice().toString())) {
                throw ExUtils.error(LocaleKey.SYS_PARAM_ERROR);
            }
        }
        String[] Sections = StrUtil.split(secondsContractEntity.getSections(), ",");
        if (!ArrayUtil.contains(Sections, dto.getSection())) {
            throw ExUtils.error(LocaleKey.SYS_PARAM_ERROR);
        }
        BigDecimal price = dto.getPrice();
        dto.setPrice(price.abs());//取绝对值
        // 获取当前货币价格（secondsContractEntity.getSymbol()）
        BigDecimal urrencyMarketTrade = secondsContractOrderService.getCurrencyMarketTrade(secondsContractEntity.getCoinPair());
        // 获取用户未开奖订单数
        int count = secondsContractOrderService.incompleteOrder(BizConst.SecondsContract.STATE_STAKE, getLoginUser(request));
        if (IntUtils.compare(count, BizConst.SecondsContract.ORDER_COUNT)) {
            throw ExUtils.error(LocaleKey.SC_ORDER_TOP_LIMIT);
        }
        // 是否c1实名认证
        UserDTO user = getLoginUser(request);
        UserKycEntity entity = kycService.findByCondition(BaseDTO.builder()
                                                    .state( BizConst.BIZ_STATUS_FAILED)
                                                    .userId( user.getId()).build());


        UserEntity userEntity=userMapper.selectById(user.getId());
        if(userEntity.getState().equals(2)) throw ExUtils.error(LocaleKey.USER_LOCKED);

        if (ObjectUtil.isEmpty( entity) || BizConst.KycConst.K1_OK > entity.getStep()) {
            throw ExUtils.error(LocaleKey.USER_C1_NOT_ACCESS);
        }
        // 
        // 通过symbol和userid获取用户特定钱包
        UserWalletEntity walletEntity = userWalletService.queryUserWallet(
                UserWalletDTO.builder().symbol(dto.getPayCoin()).userId(user.getId()).build()
        );
        // 验证余额是否足够
        BizUtils.verifyWallet(walletEntity, dto.getPrice(), BizConst.WalletConst.WALLET_TYPE_T);

        //创建订单对象
        SecondsContractOrderEntity sCOEntity = BeanUtil.toBean(dto, SecondsContractOrderEntity.class);
        sCOEntity.setUserId(user.getId());
        sCOEntity.setPayCoin(dto.getPayCoin());
        sCOEntity.setOrderNumber(IdUtil.simpleUUID());
        sCOEntity.setCoinPair(secondsContractEntity.getCoinPair());
        sCOEntity.setState(BizConst.SecondsContract.STATE_STAKE);
        sCOEntity.setSection(IntUtils.toInt(dto.getSection()));
        String Profit = StrUtil.split(secondsContractEntity.getProfit(), ",")[ArrayUtil.indexOf(Sections, dto.getSection())];
        sCOEntity.setProfit(new BigDecimal(Profit));
        sCOEntity.setCreateTime(new Date());
        sCOEntity.setStartPrice(urrencyMarketTrade);//new BigDecimal("51")下注时的价格
        sCOEntity.setDealingTime(new Date());//下注时的时间
        sCOEntity.setIsWin(BizConst.SecondsContract.RESULT_UNKNOWN);

        if(sCOEntity.getPayCoin().equals("USDT")&&sCOEntity.getSection().equals(60)&&sCOEntity.getPrice().compareTo(BigDecimal.valueOf(100))>0){

            throw ExUtils.error(LocaleKey.SYS_ERROR);

        }

        //todo  urrencyMarketTrade.getPrice() new BigDecimal("51")

        sCOEntity.setToUsdt(urrencyMarketTrade);
        sCOEntity.setSettlementTime(new Date(sCOEntity.getDealingTime().getTime()+(IntUtils.toInt(dto.getSection()) )* AppConst.SECONDS));

        //添加订单(扣款)
        if (!secondsContractOrderService.addSecondsContractOrderEntity(sCOEntity)) {
            throw ExUtils.error(LocaleKey.SYS_ERROR);
        }
        //判断锁仓任务
        //(当天5次BTB,每次数额为锁币的1%.才算完成)
        unlockWarehouseFlowService.taskDemand(sCOEntity);
        data.setData(sCOEntity);
        secondsContractTast.addSecondsContractOrder(sCOEntity);
        return data;
    }

    /**
     * 分页获取我的秒合约订单.订单状态和时间排序
     */
    @RequestMapping("/actives")
    public ResponseData actives(@RequestBody PageDTO dto, HttpServletRequest request) {
        ResponseData data = ResponseData.ok();

        if (IntUtils.isEmpty(dto.getPageNum()) || IntUtils.isEmpty(dto.getPageSize())) {
            throw ExUtils.error(LocaleKey.SYS_PARAM_ERROR);
        }

        IPage<SecondsContractOrderEntity> ipage = secondsContractOrderService.actives(dto, getLoginUser(request));

         List<JSONObject>results= ipage.getRecords().stream().map (x->{
             JSONObject result= JSON.parseObject(JSON.toJSONString( x));
             result.put("countDownTime",(( x).getSettlementTime().getTime()-System.currentTimeMillis())/1000);
             return result;
         }).collect(Collectors.toList());

         IPage<JSONObject> pageResult = new Page<>(dto.getPageNum(),dto.getPageSize());

         data.setData(pageResult);

         pageResult.setRecords(results);

         return data;
    }

    /**
     * 获取我的进行中的秒合约订单(最多5个)
     */
    @RequestMapping("/activeings")
    public ResponseData activeings(HttpServletRequest request) {
        ResponseData data = ResponseData.ok();

        List<SecondsContractOrderEntity> actives = secondsContractOrderService.activeings(getLoginUser(request));
        List<JSONObject>results= actives.stream().map (x-> {
                    JSONObject result = JSON.parseObject(JSON.toJSONString(x));
                    result.put("countDownTime", ((x).getSettlementTime().getTime() - System.currentTimeMillis()) / 1000);
                    return result;
                }
        ).collect(Collectors.toList());
        data.setData(results);

        return data;
    }

    /**
     * 合约排行榜
     */
    @RequestMapping("/favor")
    public ResponseData favor(HttpServletRequest request) {
        ResponseData data = ResponseData.ok();

//        List<SecondsContractOrderEntity> actives = secondsContractOrderService.activeings( getLoginUser(request));
//        data.setData(actives);

        return data;
    }

    /**
     * 自选的合约排行榜
     */
    @RequestMapping("/quotation")
    public ResponseData quotation(HttpServletRequest request) {
        ResponseData data = ResponseData.ok();

//        List<SecondsContractOrderEntity> actives = secondsContractOrderService.activeings( getLoginUser(request));
//        data.setData(actives);

        return data;
    }


}
