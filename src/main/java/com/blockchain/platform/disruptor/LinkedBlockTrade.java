package com.blockchain.platform.disruptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.exception.BcException;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.plugins.spring.SpringPlugin;
import com.blockchain.platform.pojo.entity.OrderFlowEntity;
import com.blockchain.platform.pojo.entity.TradeFlowEntity;
import com.blockchain.platform.pojo.vo.KLineInsertMessageVo;
import com.blockchain.platform.service.ITradeService;
import com.blockchain.platform.service.impl.KlineService;
import com.blockchain.platform.utils.BigDecimalUtils;
import com.blockchain.platform.utils.BizUtils;
import com.blockchain.platform.utils.IntUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 撮合交易队列
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-19 8:33 PM
 **/
@Log4j2
public class LinkedBlockTrade {

    public String pairName;


    public String symbol;


    final Object tradeLock = new Object();

    /**
     * 默认汇率
     */
    private BigDecimal fee = BizConst.TradeConst.FINAL_FEERATE;

    /**
     * 撮合队列
     */
    LinkedBlockingQueue<OrderFlowEntity> match = new LinkedBlockingQueue<OrderFlowEntity>();


    ExecutorService pool = Executors.newFixedThreadPool(100);


    /**
     * 清算队列
     */
    LinkedBlockingQueue<LiquidationDTO> liquidation = new LinkedBlockingQueue<LiquidationDTO>();


    public void init() {

        pool.submit(new Producer());


    }

    /**
     * 放入队列
     *
     * @param entity
     */
    public synchronized void publish(OrderFlowEntity entity) {
        try {
            match.put(entity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 交易记录
     */
    private volatile ITradeService tradeService;

    /**
     * 获取交易
     *
     * @return
     */
    private ITradeService getTradeService() {

        if (ObjectUtil.isEmpty(tradeService)) {
            tradeService = SpringPlugin.getBean(ITradeService.class);
        }
        return tradeService;
    }

    /**
     * Kline数据
     */
    private KlineService klineService;

    private KlineService getKlineService() {
        if (ObjectUtil.isEmpty(klineService)) {
            klineService = SpringPlugin.getBean(KlineService.class);
        }
        return klineService;
    }

    /**
     * 生产
     */
    class Producer implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(100);

                    synchronized (tradeLock) {


                        OrderFlowEntity event = null;
                        OrderFlowEntity buyOrder = new OrderFlowEntity();
                        OrderFlowEntity sellOrder = new OrderFlowEntity();
                        buyOrder.setSymbol(pairName);
                        buyOrder.setCoinPair(pairName);


                        buyOrder.setSymbol(symbol);
                        sellOrder.setSymbol(symbol);

                        buyOrder.setLimit(1);
                        sellOrder.setLimit(1);
                        sellOrder.setSymbol(pairName);
                        sellOrder.setCoinPair(pairName);
                        buyOrder.setType(BizConst.TradeConst.TRADE_TYPE_INPUTS_EN);
                        sellOrder.setType(BizConst.TradeConst.TRADE_TYPE_OUTS_EN);

                        List<OrderFlowEntity> buyList = getTradeService().getOrderListByType(buyOrder);
                        List<OrderFlowEntity> sellList = getTradeService().getOrderListByType(sellOrder);
                        // 都为空
                        if ( CollUtil.isEmpty( buyList) && CollUtil.isEmpty( sellList)) continue;

                        if ( CollUtil.isEmpty( buyList) && CollUtil.isNotEmpty( sellList)) {
                            OrderFlowEntity entity = sellList.get( 0);
                            entity.setCoinPair( pairName);
                            getTradeService().executeNotify( entity);
                        }

                        if ( CollUtil.isNotEmpty( buyList) && CollUtil.isEmpty( sellList)) {
                            OrderFlowEntity entity = buyList.get( 0);
                            entity.setCoinPair( pairName);
                            getTradeService().executeNotify( entity);
                        }

                        if ( CollUtil.isEmpty( buyList) || CollUtil.isEmpty( sellList)) {
                            continue;
                        }
                        buyOrder = buyList.get(0);
                        sellOrder = sellList.get(0);
                        sellOrder.setCoinPair(pairName);
                        buyOrder.setCoinPair(pairName);
                        sellOrder.setSymbol(pairName);
                        buyOrder.setSymbol(pairName);

                        event = buyOrder.getId() > sellOrder.getId() ? buyOrder : sellOrder;

                        if (event.getNum().compareTo(event.getSurplusNum()) == 0) event.setNews(true);
                        if (event.getState() == 2) continue;

                        event.setLimit(40);

                        if (StrUtil.equals(BizConst.TradeConst.TRADE_TYPE_INPUTS_EN, event.getType()) ||
                                StrUtil.equals(BizConst.TradeConst.TRADE_TYPE_OUTS_EN, event.getType())) {

                            List<Integer> ids = new ArrayList<>();
                            ids.add(event.getUserId());
                            //购买/出售 处理
                            if (IntUtils.equals(BizConst.TradeConst.ORDER_TYPE_PENDING, event.getState())) {
                                //如果是买单
                                if (BizConst.TradeConst.TRADE_TYPE_INPUTS_EN.equals(event.getType())) {
                                    event.setSymbol(BizUtils.market(event.getCoinPair()));
                                    /**
                                     *  遍历所有卖单,寻找与此买单匹配的卖单
                                     */
                                    event.setType(BizConst.TradeConst.TRADE_TYPE_OUTS_EN);
                                    List<OrderFlowEntity> sellOrders = getTradeService().getOrderListByType(event);//sql通过价格排序
                                    if (!CollUtil.isEmpty(sellOrders)) {
                                        event.setType(BizConst.TradeConst.TRADE_TYPE_INPUTS_EN);
                                        buyEvent(event, sellOrders, ids);
                                    } else {
                                        getTradeService().executeNotify( event);
                                    }
                                } else {//卖单处理方法
                                    event.setSymbol(BizUtils.token(event.getCoinPair()));
                                    event.setType(BizConst.TradeConst.TRADE_TYPE_INPUTS_EN);
                                    List<OrderFlowEntity> buyOrders = getTradeService().getOrderListByType(event);//sql通过价格排序
                                    if (!CollUtil.isEmpty(buyOrders)) {
                                        event.setType(BizConst.TradeConst.TRADE_TYPE_OUTS_EN);
                                        sellEvent(event, buyOrders, ids);
                                    }else {
                                        getTradeService().executeNotify( event);
                                    }
                                }
                            }
                        }
                        getTradeService().executeNotify( event);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }


    public void cancelOrder(OrderFlowEntity orderFlowEntity) {

        synchronized (tradeLock) {
            OrderFlowEntity orderEntity = getTradeService().viewEntity(orderFlowEntity);

            if (!BeanUtil.isEmpty(orderEntity)) {
                orderEntity.setType(orderFlowEntity.getType());
                orderEntity.setCoinPair(orderFlowEntity.getCoinPair());
                orderEntity.setSymbol(orderEntity.getSymbol());
                orderEntity.setState(orderFlowEntity.getState());
                orderEntity.setCancelUserId(orderEntity.getUserId());
                orderEntity.setCancelDate(DateUtil.date());

                if (orderEntity.getType().equals(BizConst.TradeConst.TRADE_TYPE_INPUTS_EN)) {
                    orderEntity.setTotalPrice(orderEntity.getSurplusPrice());
                } else {
                    orderEntity.setTotalPrice(orderEntity.getSurplusNum());
                }

                try {
                    getTradeService().cancelOrder(orderEntity);
                } catch (Exception e) {

                    e.printStackTrace();
                }
                // 撤单用户
                orderEntity.setDealIds(new Integer[]{orderEntity.getUserId()});
            }
            getTradeService().executeNotify(orderEntity);
        }
    }


    /**
     * 买单撮合交易方法
     *
     * @param event
     * @throws Exception
     */
    @Transactional
    public void buyEvent(OrderFlowEntity event, List<OrderFlowEntity> sellOrders, List<Integer> ids) throws Exception {

        List<TradeFlowEntity> flowEntities = new ArrayList<>();
        List<OrderFlowEntity> orderEntities = new ArrayList<>();

        //订单单价
        BigDecimal price = event.getPrice();

        //买单手续费汇率
        BigDecimal buyFee = BigDecimalUtils.multi(fee, event.getFee());

        for (int i = 0; i < sellOrders.size(); i++) {
            //订单总价
            BigDecimal totalNum = event.getSurplusNum();
            //本次撮合的计算基础订单
            OrderFlowEntity dealOrder = sellOrders.get(i);
            //如果订单单价大于或者等于卖一价，就需要撮合交易
            if (price.compareTo(dealOrder.getPrice()) >= 0) {
                event.setMatch( Boolean.TRUE);
                // 是否执行了撮合
                ids.add(dealOrder.getUserId());
                //当前卖单总价
                BigDecimal sellTotalNum = dealOrder.getSurplusNum();

                //卖单手续费费率
                BigDecimal sellFee = BigDecimalUtils.multi(fee, dealOrder.getFee());

                //实例化流水对象，将不变值存入实体对象
                TradeFlowEntity trade = new TradeFlowEntity(dealOrder.getPrice(), BizConst.TradeConst.TRADE_TYPE_INPUTS_EN, event.getUserId(), event.getId(), dealOrder.getUserId(), dealOrder.getId(), buyFee, sellFee);
                //默认买一不能全部购买卖一单，以买一作为计算基数,
                BigDecimal baseTotalNum = totalNum;
                //当前订单总价大于或者等于卖一总价，则卖一订单全部成交
                //卖一单全部成交的情况下，以卖一单作为计算基数
                if (totalNum.compareTo(sellTotalNum) >= 0) {
                    baseTotalNum = sellTotalNum;
                    dealOrder.setState(BizConst.TradeConst.ORDER_TYPE_FINISH);
                    dealOrder.setSurplusPrice(BigDecimal.ZERO);
                    dealOrder.setSurplusNum(BigDecimal.ZERO);
                } else {
                    //剩余出售代币可以兑换的资金总额
                    dealOrder.setSurplusPrice(BigDecimalUtils.subtr(dealOrder.getSurplusPrice(), BigDecimalUtils.multi(baseTotalNum, dealOrder.getPrice())));
                    //更新撮合后的交易买单（代币数量）
                    dealOrder.setSurplusNum(BigDecimalUtils.subtr(dealOrder.getSurplusNum(), baseTotalNum));
                }

                //买家需要解冻的基础货币数量（实际购买花费的数量有可能跟解冻的数量不一致）
                event.setUnfreezePrice(BigDecimalUtils.add(event.getUnfreezePrice(), BigDecimalUtils.multi(baseTotalNum, price)));
                //买单需要减去的基础货币剩余购买金额
                event.setSurplusPrice(BigDecimalUtils.subtr(event.getSurplusPrice(), BigDecimalUtils.multi(baseTotalNum, price)));


                //本次撮合交易的总价
                BigDecimal totalPrice = BigDecimalUtils.multi(baseTotalNum, dealOrder.getPrice());
                // 买家手续费
                BigDecimal buyCharge = BigDecimalUtils.multi(baseTotalNum, buyFee);
                trade.setBuyCharge(buyCharge);
                //卖家手续费
                BigDecimal sellCharge = BigDecimalUtils.multi(totalPrice, sellFee);
                trade.setSellCharge(sellCharge);
                //本次交易撮合数量
                trade.setNum(baseTotalNum);
                //本次交易撮合金额
                trade.setTotalPrice(totalPrice);
                //主动撮合类型（交易记录显示绿/红色）
                trade.setType(BizConst.TradeConst.TRADE_TYPE_INPUTS_EN);
                //撮合完毕，将撮合交易流水放入集合，以待下步处理
                trade.setDealTime(System.currentTimeMillis());
                flowEntities.add(trade);

                //本次交易买家实际获得代币数量
                BigDecimal buyDealNum = BigDecimalUtils.subtr(baseTotalNum, buyCharge);

                dealOrder.setBuyCoinNum(buyDealNum);
                //本次交易买家实际支付基础货币数量
                dealOrder.setBuyBaseCoinNum(totalPrice);
                //本次交易卖家实际支付代币数量
                dealOrder.setSellCoinNum(baseTotalNum);
                //本次交易卖家实际获得基础货币数量
                BigDecimal sellDealPrice = BigDecimalUtils.multi(buyDealNum, dealOrder.getPrice());
                dealOrder.setSellBaseCoinNum(sellDealPrice);


                orderEntities.add(dealOrder);

                //买单变化
                event.setSurplusNum(BigDecimalUtils.subtr(event.getSurplusNum(), baseTotalNum));
                //买家需要的更新的代币数量
                event.setBuyCoinNum(BigDecimalUtils.add(event.getBuyCoinNum(), buyDealNum));
                //买家需要减去的基础货币数量（加手续费的成交额）
                event.setBuyBaseCoinNum(BigDecimalUtils.add(event.getBuyBaseCoinNum(), totalPrice));

                event.setType(BizConst.TradeConst.TRADE_TYPE_INPUTS_EN);

                if (event.getSurplusNum().compareTo(BigDecimal.ZERO) == 0) {
                    event.setState(BizConst.TradeConst.ORDER_TYPE_FINISH);
                    event.setSurplusPrice(BigDecimal.ZERO);
                    event.setSurplusNum(BigDecimal.ZERO);
                    break;
                }
            } else {
                break;
            }
        }
        event.setDealIds(ids.toArray(new Integer[]{ids.size()}));

        //批量处理交易记录
        doClearAndUpdate(event, flowEntities, orderEntities);
    }

    private void doClearAndUpdate(OrderFlowEntity event, List<TradeFlowEntity> flowEntities, List<OrderFlowEntity> orderEntities) throws Exception {
        if (flowEntities.size() > 0) {
            // 通知用户列表
            makeKlineAndAddTradeFlow(event, flowEntities, orderEntities);
            getKlineService().doKlineInsert(new KLineInsertMessageVo(event.getCoinPair().toUpperCase(), flowEntities.get(0).getPrice(), event.getNum(), flowEntities.get(0).getDealTime()));
            if(flowEntities.size()>1){
                getKlineService().doKlineInsert(new KLineInsertMessageVo(event.getCoinPair().toUpperCase(), flowEntities.get(flowEntities.size()-1).getPrice(), BigDecimal.ZERO, flowEntities.get(flowEntities.size()-1).getDealTime()));

            }

        } else {
            getTradeService().executeNotify(event);
        }
    }


    /**
     * 卖单撮合交易方法
     *
     * @param event
     * @throws Exception
     */

    @Transactional
    public void sellEvent(OrderFlowEntity event, List<OrderFlowEntity> orderlist, List<Integer> ids) throws Exception {

        List<TradeFlowEntity> flowEntities = new ArrayList<>();
        List<OrderFlowEntity> orderEntities = new ArrayList<>();

        //订单单价
        BigDecimal price = event.getPrice();
        //订单类型
        String orderType = event.getType();

        //卖单手续费汇率
        BigDecimal sellFee = BigDecimalUtils.multi(fee, event.getFee());

        for (int i = 0; i < orderlist.size(); i++) {
            //订单总价
            BigDecimal totalNum = event.getSurplusNum();
            //当前卖单计算基础订单
            OrderFlowEntity dealOrder = orderlist.get(i);

            //如果订单单价小于或者等于买一价，就需要撮合交易
            if (price.compareTo(dealOrder.getPrice()) <= 0) {
                event.setMatch( Boolean.TRUE);
                ids.add(dealOrder.getUserId());
                //当前买单总价（基础货币总价）
                BigDecimal buyTotalNum = dealOrder.getSurplusNum();
                //买单手续费汇率
                BigDecimal buyFee = BigDecimalUtils.multi(fee, dealOrder.getFee());
                //实例化流水对象，将不变值存入实体对象
                TradeFlowEntity trade = new TradeFlowEntity(dealOrder.getPrice(), orderType, dealOrder.getUserId(), dealOrder.getId(), event.getUserId(), event.getId(), buyFee, sellFee);

                //默认卖一不能全部出售买一单，以卖一作为计算基数,
                BigDecimal baseTotalNum = totalNum;

                //本次撮合交易的总价
                BigDecimal totalPrice = BigDecimalUtils.multi(baseTotalNum, dealOrder.getPrice());

                //当前订单总数量大于或者等于卖一总价，则卖一订单全部成交
                //卖一单全部成交的情况下，以卖一单作为计算基
                if (totalNum.compareTo(buyTotalNum) >= 0) {
                    baseTotalNum = buyTotalNum;
                    //本次撮合交易的总价
                    totalPrice = dealOrder.getSurplusPrice();

                    dealOrder.setState(BizConst.TradeConst.ORDER_TYPE_FINISH);
                    dealOrder.setSurplusPrice(BigDecimal.ZERO);
                    dealOrder.setSurplusNum(BigDecimal.ZERO);

                    event.setSurplusPrice(BigDecimalUtils.subtr(event.getSurplusPrice(), totalPrice));

                } else {
                    //更新撮合后的交易买单（代币数量）
                    dealOrder.setSurplusNum(BigDecimalUtils.subtr(dealOrder.getSurplusNum(), baseTotalNum));
                    //更新撮合后交易买单（基础货币数量）
                    dealOrder.setSurplusPrice(BigDecimalUtils.subtr(dealOrder.getSurplusPrice(), totalPrice));

                    event.setSurplusPrice(BigDecimal.ZERO);

                }
                trade.setDealTime(System.currentTimeMillis());
                //买家手续费
                BigDecimal buyCharge = BigDecimalUtils.multi(baseTotalNum, buyFee);
                trade.setBuyCharge(buyCharge);
                //卖家手续费
                BigDecimal sellCharge = BigDecimalUtils.multi(totalPrice, sellFee);
                trade.setSellCharge(sellCharge);
                //本次交易撮合数量
                trade.setNum(baseTotalNum);
                //本次交易撮合金额
                trade.setTotalPrice(totalPrice);
                //主动撮合类型（交易记录显示绿/红色）
                trade.setType(BizConst.TradeConst.TRADE_TYPE_OUTS_EN);
                //撮合完毕，将撮合交易流水放入集合，以待下步处理
                flowEntities.add(trade);

                //本次交易买家实际获得代币数量
                BigDecimal buyDealNum = BigDecimalUtils.subtr(baseTotalNum, buyCharge);
                dealOrder.setBuyCoinNum(buyDealNum);
                //本次交易买家实际支付基础货币数量
                dealOrder.setBuyBaseCoinNum(totalPrice);
                //本次交易卖家实际支付代币数量
                dealOrder.setSellCoinNum(baseTotalNum);
                //本次交易卖家实际获得基础货币数量
                BigDecimal sellDealPrice = BigDecimalUtils.subtr(totalPrice, sellCharge);
                dealOrder.setSellBaseCoinNum(sellDealPrice);

                orderEntities.add(dealOrder);

                //卖单变化，数量（代币）
                event.setSurplusNum(BigDecimalUtils.subtr(event.getSurplusNum(), baseTotalNum));
                //卖家需要的更新的代币数量
                event.setSellCoinNum(BigDecimalUtils.add(event.getSellCoinNum(), baseTotalNum));
                //卖家需要更新的基础货币数量
                event.setSellBaseCoinNum(BigDecimalUtils.add(event.getSellBaseCoinNum(), sellDealPrice));

                event.setType(BizConst.TradeConst.TRADE_TYPE_OUTS_EN);

                if (event.getSurplusNum().compareTo(BigDecimal.ZERO) == 0) {
                    event.setState(BizConst.TradeConst.ORDER_TYPE_FINISH);
                    break;
                }
            } else {
                break;
            }
        }

        event.setDealIds(ids.toArray(new Integer[]{ids.size()}));
        //如果有交易记录，更新撮合交易的
        doClearAndUpdate(event, flowEntities, orderEntities);

    }

    @Transactional
    public void makeKlineAndAddTradeFlow(OrderFlowEntity event, List<TradeFlowEntity> flowEntities, List<OrderFlowEntity> orderEntities) throws Exception {
        Map<String, Object> map = CollUtil.newHashMap();
        map.put("tradeList", flowEntities);
        map.put("orderList", orderEntities);
        map.put("order", event);
        // 肯定执行了撮合
        event.setMatch( Boolean.TRUE);
        getTradeService().addTradeFlowList(map);
        getTradeService().executeNotify(event);

    }


    /**
     * 消费
     */
    public class Consumer implements Runnable {
        @Override
        public void run() {

//            while ( true) {

//                try {
//
//                    LiquidationDTO dto = liquidation.take();
//
//                    List<TradeFlowEntity> flowEntities = dto.getFlowEntities();
//
//                    if ( CollUtil.isNotEmpty( flowEntities)) {
//                        Map<String, Object> map = CollUtil.newHashMap();
//                        map.put("tradeList", flowEntities);
//                        map.put("orderList", dto.getOrderEntities());
//                        map.put("order", dto.getEntity());
//
//                        getTradeService().addTradeFlowList(map);
//
//                        for (TradeFlowEntity entity : flowEntities) {
//                            getKlineService().doKlineInsert(new KLineInsertMessageVo( dto.getEntity().getCoinPair(),
//                                    entity.getPrice(), entity.getNum(), System.currentTimeMillis()));
//                        }
//                    }
//                    // 开始通知
//                    getTradeService().executeNotify( dto.getEntity());
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
        }
    }

}
