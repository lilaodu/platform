package com.blockchain.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.blockchain.platform.config.SlbConfig;
import com.blockchain.platform.constant.*;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.pojo.dto.*;
import com.blockchain.platform.pojo.entity.CoinEntity;
import com.blockchain.platform.pojo.entity.TradeFlowEntity;
import com.blockchain.platform.pojo.vo.*;
import lombok.extern.slf4j.Slf4j;
import com.blockchain.platform.exception.BcException;
import com.blockchain.platform.mapper.OrderFlowMapper;
import com.blockchain.platform.mapper.TradeMapper;
import com.blockchain.platform.mapper.UserWalletMapper;
import com.blockchain.platform.pojo.entity.OrderFlowEntity;
import com.blockchain.platform.service.ITradeService;
import com.blockchain.platform.utils.BizUtils;
import com.blockchain.platform.utils.IntUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 交易service实现类
 */
@Slf4j
@Service
public class TradeServiceImpl implements ITradeService {

    @Resource
    private TradeMapper tradeMapper;

    @Resource
    private UserWalletMapper userWalletMapper;

    @Resource
    private OrderFlowMapper orderMapper;

    @Resource
    private RedisPlugin redisPlugin;

    /**
     * slb配置
     */
    @Resource
    private SlbConfig config;

    @Autowired
    KlineService klineService;


    /**
     * 订单新增实现方法（需冻结对应的货币）
     * 添加订单，需要获取交易的币种，然后冻结指定的数值，之后才能添加订单
     * 1.获取交易对中的币种
     * 2.验证对应钱包里的数值
     * 3.冻结对应的数额
     * 4.添加订单
     * （需要在同一事务中进行）
     *
     * @param entity
     * @return
     */
    @Transactional
    public boolean addOrderEntity(OrderFlowEntity entity) {
        //获取交易对中的币种
        if (StrUtil.equals(BizConst.TradeConst.TRADE_TYPE_INPUTS_EN, entity.getType())) {//买入
            entity.setSymbol(BizUtils.market(entity.getCoinPair()));
        } else {//卖出
            entity.setSymbol(BizUtils.token(entity.getCoinPair()));
        }
        //冻结交易金额（sql验证数值）
        int num = userWalletMapper.frozenUserWallet(entity);
        //添加订单
        if (num > 0) {
            num = tradeMapper.addOrder(entity);
        } else {
            throw new BcException(ExConst.SYS_ERROR);
        }
        return num > 0 ? true : false;
    }


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public OrderFlowEntity viewEntity(OrderFlowEntity event) {
        OrderFlowEntity entity = tradeMapper.queryOrder(event);
        return entity;
    }


    public PageVO queryUserDeputes(PageDTO dto) {
        Map<String, Object> map = BeanUtil.beanToMap(dto);
        map.put(QueryConst.LIMIT, dto.getPageSize());
        map.put(QueryConst.PAGE, dto.getPageNum());
        Map<String, Object> totalMap = tradeMapper.queryUserDeputes(map);

        map.put(QueryConst.QUERY_USER_ID, dto.getUserId());
        map.put(QueryConst.QUERY_FROM, IntUtils.toInt(totalMap.get(QueryConst.QUERY_FROM)));
        map.put(QueryConst.QUERY_TO, IntUtils.toInt(totalMap.get(QueryConst.QUERY_TO)));

        List<Map<String, Object>> list = tradeMapper.queryDeputesPage(map);
        return PageVO.builder()
                .total(IntUtils.toLong(totalMap.get(QueryConst.QUERY_PAGE_COUNT)))
                .list(list).build();
    }

    public PageVO queryUserDeals(PageDTO dto) {

        Map<String, Object> map = BeanUtil.beanToMap(dto);
        map.put(QueryConst.LIMIT, dto.getPageSize());
        map.put(QueryConst.PAGE, dto.getPageNum());

        Map<String, Object> totalMap = tradeMapper.queryUserDeals(map);

        map.put(QueryConst.QUERY_USER_ID, dto.getUserId());
        map.put(QueryConst.QUERY_FROM, IntUtils.toInt(totalMap.get(QueryConst.QUERY_FROM)));
        map.put(QueryConst.QUERY_TO, IntUtils.toInt(totalMap.get(QueryConst.QUERY_TO)));

        List<Map<String, Object>> list = tradeMapper.queryDealsPage(map);

        return PageVO.builder()
                .total(IntUtils.toLong(totalMap.get(QueryConst.QUERY_PAGE_COUNT)))
                .list(list).build();
    }


    class BalanceChange {
        public int userId;
        public BigDecimal in;
        public BigDecimal out;
        public String inSymbol;
        public String outSymbol;

        public String getInKey() {
            return inSymbol + ":" + userId;

        }

        public String getOutKey() {
            return outSymbol + ":" + userId;

        }

        @Override
        public int hashCode() {
            return userId;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof BalanceChange) {
                return userId == ((BalanceChange) obj).userId;
            }
            return super.equals(obj);
        }
    }


    List<BalanceChange> mergeBalance(List<BalanceChange> balanceChanges) {
        Map<String, BigDecimal> bigDecimalMap = new HashMap<>();
        for (BalanceChange balanceChange : balanceChanges) {
            BigDecimal inAmount = bigDecimalMap.getOrDefault(balanceChange.getInKey(), BigDecimal.ZERO);
            BigDecimal outAmount = bigDecimalMap.getOrDefault(balanceChange.getOutKey(), BigDecimal.ZERO);
            inAmount = balanceChange.in.add(inAmount);
            outAmount = outAmount.subtract(balanceChange.out);
            bigDecimalMap.put(balanceChange.getInKey(), inAmount);
            bigDecimalMap.put(balanceChange.getOutKey(), outAmount);
        }


        balanceChanges = balanceChanges.stream().distinct().collect(Collectors.toList());


        for (BalanceChange balanceChange : balanceChanges) {
            balanceChange.out = bigDecimalMap.get(balanceChange.getOutKey());
            balanceChange.in = bigDecimalMap.get(balanceChange.getInKey());


        }


        return balanceChanges;


    }

    /**
     * 交易撮合后的，数据库更新，将撮合的整个过程数据更新 放在同一个事务
     *
     * @param map(交易集合/需要修改的订单集合/撮合标准订单类型（BUY orderlist为selllist，SELL orderlist为BUY)
     * @return
     */
    @Transactional
    public boolean addTradeFlowList(Map<String, Object> map) {
        try {
            OrderFlowEntity order = (OrderFlowEntity) map.get("order");
            String coin = BizUtils.token(order.getCoinPair());

            List<TradeFlowEntity> tradeFlowEntities = (List<TradeFlowEntity>) map.get("tradeList");

            String baseCoin = BizUtils.market(order.getCoinPair());
            List<BalanceChange> balanceChanges = new ArrayList<>(tradeFlowEntities.size() * 2);
            for (int i = 0; i < tradeFlowEntities.size(); i++) {
                TradeFlowEntity entity = tradeFlowEntities.get(i);
                int buyUserId = entity.getBuyUserId();
                int sellUserid = entity.getSellUserId();
                String symbol = coin;
                String market = baseCoin;
                BalanceChange sellBalanceChange = new BalanceChange();
                BalanceChange buyBalanceChange = new BalanceChange();
                sellBalanceChange.userId = sellUserid;
                sellBalanceChange.inSymbol = market;
                sellBalanceChange.outSymbol = symbol;
                buyBalanceChange.userId = buyUserId;
                buyBalanceChange.outSymbol = market;
                buyBalanceChange.inSymbol = symbol;
                sellBalanceChange.in = entity.getTotalPrice().subtract(entity.getSellCharge());
                sellBalanceChange.out = entity.getNum();
                buyBalanceChange.in = entity.getNum().subtract(entity.getBuyCharge());
                buyBalanceChange.out = entity.getTotalPrice();
                balanceChanges.add(sellBalanceChange);
                balanceChanges.add(buyBalanceChange);
            }
            int flag = 0;
//            balanceChanges=mergeBalance(balanceChanges);
            balanceChanges.sort((x1, x2) -> {
                if (x1.userId > x2.userId) return 1;
                else if (x1.userId == x2.userId) return 0;
                else return 1;
            });
            for (BalanceChange balanceChange : balanceChanges) {
                flag = userWalletMapper.updateAssets(UserWalletDTO.builder()
                        .userId(balanceChange.userId)
                        .symbol(balanceChange.outSymbol)
                        .balance(balanceChange.out.abs())
                        .frozenBalance(balanceChange.out.negate())
                        .build());
                if (flag == 0) throw new BcException(ExConst.TRADE_ORDER_COIN_ERROR);

                flag = userWalletMapper.updateAssets(UserWalletDTO.builder()
                        .userId(balanceChange.userId)
                        .symbol(balanceChange.inSymbol)
                        .balance(balanceChange.in.negate())
                        .frozenBalance(BigDecimal.ZERO)
                        .build());
                if (flag == 0) throw new BcException(ExConst.TRADE_ORDER_COIN_ERROR);

            }
            //买单退币
            if (order.getType().equalsIgnoreCase(BizConst.TradeConst.TRADE_TYPE_INPUTS_EN) && order.isNews()) {
                order.setTotalPrice(order.getPrice().multiply(order.getNum()));
                BigDecimal remindMarket = order.getTotalPrice().subtract(order.getBuyBaseCoinNum()).subtract((order.getSurplusNum().multiply(order.getPrice())));
                remindMarket=remindMarket.negate();
                if (remindMarket.compareTo(BigDecimal.ZERO) > 0) {
                    flag = userWalletMapper.updateAssets(UserWalletDTO.builder()
                            .userId(order.getUserId())
                            .symbol(baseCoin)
                            .balance(BigDecimal.ZERO)
                            .frozenBalance(remindMarket)
                            .build());
                    if (flag == 0) throw new BcException(ExConst.TRADE_ORDER_COIN_ERROR);
                }

            }
            order.setMatch(true);


            // 交易币
            // 交易货币
            order.setSymbol(order.getCoinPair());

            Map<String, Object> param = new HashMap<String, Object>();
            int num = 0;

            param.put(QueryConst.QUERY_TYPE, order.getType());
            param.put(QueryConst.QUERY_SYMBOL, baseCoin);
            param.put(QueryConst.QUERY_SYMBOL, coin);

            param.put("data", map.get("orderList"));
            //5、更新计算基数的买卖单列表
            num = tradeMapper.mdfOrder(order);
            if (num == 0) {
                throw new BcException(ExConst.TRADE_ORDERS_ERROR);
            }
            //6、更新计算基数的代币钱包
            order.setSymbol(coin);

            //7、更新计算基数的基础货币钱包
            order.setSymbol(baseCoin);

            param.put(QueryConst.QUERY_SYMBOL, order.getCoinPair());
            //2、更新买卖单集合信息
            param.put("coinPair", order.getCoinPair());
            num = tradeMapper.mdfOrderList(param);
            if (num == 0) {
                throw new BcException(ExConst.TRADE_ORDERS_ERROR);
            }
            param.put("data", map.get("tradeList"));
            //1、存入撮合后交易 无论买入还是卖出，交易流水都正常进行
            num = tradeMapper.addTradeFlows(param);
            if (num == 0) {
                throw new BcException(ExConst.TRADE_FLOWS_ERROR);
            } else {
                return true;
            }

        } catch (BcException e) {
            throw e;
        }
    }

    public List<OrderFlowEntity> getOrderListByType(OrderFlowEntity event) {
        if(event!=null&&event.getSymbol()!=""&&event.getType()!=""){
            return tradeMapper.queryOrderListByType(event);
        }
        List<OrderFlowEntity> en = new ArrayList<>();
        return  en;
    }

    @Transactional(rollbackFor = {Exception.class})
    public void cancelOrder(OrderFlowEntity event) throws Exception {

        //买单撤单
        if (BizConst.TradeConst.TRADE_TYPE_INPUTS_EN.equals(event.getType())) {
            event.setSymbol(BizUtils.market(event.getCoinPair()));
        }
        //卖单撤单
        else {
            event.setSymbol(BizUtils.token(event.getCoinPair()));
        }
        int num = userWalletMapper.mdfUserWalletForCancel(event);
        if (num == 0) {
            throw new BcException(ExConst.TRADE_ORDERS_ERROR);
        }
        num = tradeMapper.cancelOrder(event);
        if (num == 0) {
            throw new BcException(ExConst.TRADE_ORDERS_ERROR);
        }

    }

    public List<DealVO> queryDeals(BaseDTO dto) {
        return orderMapper.queryDeals(dto);
    }


    public List<WsOrderDTO> queryHandicap(BaseDTO dto) {
        return orderMapper.queryHandicap(dto);
    }

    @Override
    public RankingVO queryRankingByCondition(TickDTO dto) {
        Map<String, Object> tick = orderMapper.queryTick(BeanUtil.beanToMap(dto));
        return BeanUtil.toBean(tick, RankingVO.class);
    }


    final static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    public void doNotify(OrderFlowEntity event) {
        try {
            if (event == null) return;
            ;
            WsVO vo = WsVO.builder().build();
            // 开始执行
            TimeUnit.MILLISECONDS.sleep(BizConst.DEFAULT_SLEEP_TIME);
            // 切换交易对
            String ns = event.getCoinPair();
            String market = BizUtils.market(ns);
            String token = BizUtils.token(ns);

            // 货币配置
            Map<String, CoinEntity> config = redisPlugin.get(RedisConst.PLATFORM_COIN_CONFIG);
            CoinEntity entity = config.get(market);

            // 更新 当前交易对 tick
//            if (event.isMatch()) {
            Long ts = DateUtil.parse(DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN)).getTime() / 1000L;

            RankingVO ranking = queryRankingByCondition(
                    TickDTO.builder().ts(ts).now(DateUtil.currentSeconds()).symbol(event.getCoinPair()).build());

            // 复制数据
            BeanUtil.copyProperties(config.get(token), ranking);

            ranking.setMarket(market);
            // 更新 redis

            List<RankingVO> list = redisPlugin.hget(RedisConst.PLATFORM_RANKING, market);

            List<RankingVO> nList = CollUtil.newArrayList();

            for (int idx = 0; list != null && idx < list.size(); idx++) {
                RankingVO rankingVO = list.get(idx);
                if (StrUtil.equals(rankingVO.getSymbol(), token)) {
                    nList.add(ranking);
                } else {
                    nList.add(rankingVO);
                }
            }
            redisPlugin.hset(RedisConst.PLATFORM_RANKING, market, nList);

            // 更新首页排行榜
            Map<String, List<RankingVO>> home = redisPlugin.get(RedisConst.PLATFORM_HOME_RANKING);
            //涨
            if(home!=null) {
                home.put(BizConst.Ranking.RANKING_RISE, BizUtils.sort(BizConst.Ranking.RANKING_RISE, nList));
                //跌
                home.put(BizConst.Ranking.RANKING_FALL, BizUtils.sort(BizConst.Ranking.RANKING_FALL, nList));
            }
            //存储首页的redis
            redisPlugin.set(RedisConst.PLATFORM_HOME_RANKING, home);

            vo.setTick(ranking);
//            }


            // 当前货币 数据位数
            List<String> digit = Arrays.asList(StrUtil.split(entity.getDecLength(), StrUtil.COMMA));
            List<Integer> decimal = new ArrayList<>();
//            for (String d : digit) {
//                decimal.add(IntUtils.toInt(d));
//            }

            decimal.add(IntUtils.toInt(digit.get(IntUtils.INT_ZERO)));

            // 买单数据
            BaseDTO baseDTO = BaseDTO.builder().build();
            baseDTO.setType(BizConst.TradeConst.TRADE_TYPE_INPUTS_EN);
            baseDTO.setLimit(BizConst.OrderConst.DEFAULT_HANDICAP_LENGTH);
            baseDTO.setSymbol(event.getCoinPair());
            // 买卖盘数据 状态 为 挂单
            baseDTO.setState(BizConst.TradeConst.ORDER_TYPE_PENDING);
            baseDTO.setDecimal(decimal);
            baseDTO.setId(event.getId());

            Map<String, List<OrderVO>> buys = handicapVO(baseDTO, ns);
            vo.setBuys(buys);


            // 卖盘数据
            baseDTO.setType(BizConst.TradeConst.TRADE_TYPE_OUTS_EN);
            Map<String, List<OrderVO>> sells = handicapVO(baseDTO, ns);
            vo.setSells(sells);

            baseDTO.setId(null);

            // 交易历史
//            if (event.isMatch()) {
            baseDTO.setLimit(BizConst.OrderConst.DEFAULT_HISTORY_LENGTH);
            List<DealVO> deals = queryDeals(baseDTO);

            redisPlugin.hset(RedisConst.PLATFORM_HISTORY, ns, deals);

            vo.setHistory(deals);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程池
     */
    @Override
    public void executeNotify(OrderFlowEntity event) {

        doNotify(event);


    }


    /**
     * 通知地址
     *
     * @return
     */
    protected List<String> slb() {
        List<String> ips = CollUtil.newArrayList();
        String[] values = StrUtil.split(config.getIpConfig(), StrUtil.COMMA);

        for (String ip : values) {
            ips.add(StrUtil.concat(Boolean.FALSE, ip, StrUtil.COLON,
                    config.getPort(),
                    config.getContextPath(),
                    config.getAction()));
        }
        return ips;
    }

    protected Map<String, List<OrderVO>> handicapVO(BaseDTO dto, String symbol) {
        Map<String, List<OrderVO>> vo = MapUtil.newHashMap();
        List<WsOrderDTO> list = queryHandicap(dto);
        for (int idx = 0; idx < list.size(); idx++) {
            WsOrderDTO wsOrderDTO = list.get(idx);
            vo.put(StrUtil.toString(wsOrderDTO.getDigit()), wsOrderDTO.getList());
        }
        // 设置缓存
        redisPlugin.hset(StrUtil.equals(BizConst.TradeConst.TRADE_TYPE_OUTS_EN,
                dto.getType()) ? RedisConst.PLATFORM_HANDICAP_SELL : RedisConst.PLATFORM_HANDICAP_BUY, symbol, vo
        );

        return vo;
    }
}