/**
 * @program: exchange
 * @description: toDo
 * @author: DengWei
 * @create: 2019-05-23
 **/
package com.blockchain.platform.task;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.blockchain.platform.exception.BcException;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.utils.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.constant.TvConst;
import com.blockchain.platform.dictionary.DictionaryFactory;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.pojo.dto.KlineDTO;
import com.blockchain.platform.pojo.entity.KlineEntity;
import com.blockchain.platform.pojo.entity.MarketCoinEntity;
import com.blockchain.platform.pojo.vo.KlineVO;
import com.blockchain.platform.service.IChartService;
import com.blockchain.platform.service.ITaskService;
import com.blockchain.platform.service.IUnlockWarehouseFlowService;
import com.blockchain.platform.service.impl.UnlockWarehouseFlowServiceimpl;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @program: TaskRunner
 * @description: 定时器任务
 * @author: DengWei
 * @create: 2019-05-23 20:33
 **/
@Component
public class TaskRunner   implements ApplicationRunner, Ordered {

    @Resource
    private DictionaryFactory factory;

    
    @Resource
    private IUnlockWarehouseFlowService unlockWarehouseFlowService;

    @Override//
    public void run(ApplicationArguments args) throws Exception {
    }

    /**
     * 每5分钟执行一次
     * @throws Exception
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void flushPrice() throws Exception {
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
    }

    /**
     * 实时更新汇率
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void huobiTicker() {
        // 秒合约信息
        factory.secondsContract( Boolean.FALSE);
    }


    /**
     * 实时更新汇率
     * 每五分钟执行一次
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void flushRates() {
        factory.rates();
    }

    
    /**
     * 锁仓销毁
     */
    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void destruction() {
    	unlockWarehouseFlowService.destruction();
    }


    @Override
    public int getOrder() {
        return 1000;
    }

}
