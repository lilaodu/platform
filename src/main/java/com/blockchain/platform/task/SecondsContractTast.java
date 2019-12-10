package com.blockchain.platform.task;


import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.mapper.SecondsContractOrderMapper;
import com.blockchain.platform.pojo.dto.SecondsContractOrderDTO;
import com.blockchain.platform.pojo.entity.SecondsContractOrderEntity;
import com.blockchain.platform.service.ISecondsContractOrderService;
import com.blockchain.platform.utils.IntUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class SecondsContractTast implements ApplicationRunner, Ordered {
    Timer timer = new Timer();
    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    @Resource
    private SecondsContractOrderMapper mapper;


    @Resource
    private ISecondsContractOrderService secondsContractOrderService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//
//        List<SecondsContractOrderEntity> list = mapper.queryUnCompleted();
//
//        for (SecondsContractOrderEntity secondsContractOrderEntity : list) {
//            addSecondsContractOrder(secondsContractOrderEntity);
//        }
    }

    public void addSecondsContractOrder(SecondsContractOrderEntity secondsContractOrderEntity) {
        if (secondsContractOrderEntity.getSettlementTime() == null) {
            secondsContractOrderEntity.setSettlementTime(new Date(secondsContractOrderEntity.getDealingTime().getTime() + (IntUtils.toInt(secondsContractOrderEntity.getSection())) * AppConst.SECONDS));
        }
        System.out.println("加入时间列队`````````````````````````````````````");
        System.out.println("秒合约时间: +" + secondsContractOrderEntity.getSettlementTime());

        long timeNow = System.currentTimeMillis();
        if (timeNow <= secondsContractOrderEntity.getSettlementTime().getTime()) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    cachedThreadPool.submit(() -> {
                        secondsContractOrderService.lottery(secondsContractOrderEntity);
                    });
                }
            }, secondsContractOrderEntity.getSettlementTime());
        } else {//取消订单,退钱
            cachedThreadPool.submit(() -> {
                secondsContractOrderService.cancelOrder(secondsContractOrderEntity);
            });

        }
    }
    @Scheduled(cron = "0 0/1 * * * ?")
    public void updateOrderFlow() {
        List<SecondsContractOrderEntity> list = mapper.queryUnCompleted();

        for (SecondsContractOrderEntity secondsContractOrderEntity : list) {
            long timeNow = System.currentTimeMillis();
            if (timeNow > secondsContractOrderEntity.getSettlementTime().getTime()+10000) {
                cachedThreadPool.submit(() -> {
                    secondsContractOrderService.cancelOrder(secondsContractOrderEntity);
                });
            }
        }
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
