package com.blockchain.platform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 货币API配置
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-09-15 7:20 PM
 **/
@Data
@Component
@ConfigurationProperties(prefix = "platform.ticker")
public class HuobiApiCfg {

    /**
     * 获取聚合ticker
     */
    private String huobiTickerApi;

    /**
     * 货币历史数据API
     */
    private String huobiHistoryApi;
}
