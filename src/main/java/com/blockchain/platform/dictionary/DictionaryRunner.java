package com.blockchain.platform.dictionary;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
//
//import com.blockchain.platform.constant.RedisConst;
//import com.huobi.client.SubscriptionClient;
//import com.huobi.client.model.enums.CandlestickInterval;

import javax.annotation.Resource;

/**
 * 交易字典
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-05-11 2:40 PM
 **/
@Component
public class DictionaryRunner implements Ordered, ApplicationRunner {

	/**
	 * 工厂类
	 */
	@Resource
	private DictionaryFactory factory;

	@Override
	public void run(ApplicationArguments args) throws Exception {
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
		factory.secondsContract( Boolean.TRUE);
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
		// 开启火币请,求获取秒合约对应市场的k线数据
		factory.huoBiWS();
		//钱包配置
		factory.wallet();
		// 升级配置
		factory.upgrade();

	}

	@Override
	public int getOrder() {
		return 10000;
	}
}
