package com.blockchain.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.math.MathUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.blockchain.platform.pojo.vo.KLinesVo;
import com.blockchain.platform.service.impl.KlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.RedisConst;
import com.blockchain.platform.constant.TvConst;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.pojo.dto.KlineDTO;
import com.blockchain.platform.pojo.dto.TvDTO;
import com.blockchain.platform.pojo.entity.CoinEntity;
import com.blockchain.platform.pojo.entity.MarketCoinEntity;
import com.blockchain.platform.pojo.vo.KlineVO;
import com.blockchain.platform.pojo.vo.TvChartVO;
import com.blockchain.platform.pojo.vo.TvConfigVO;
import com.blockchain.platform.service.IChartService;
import com.blockchain.platform.utils.BizUtils;
import com.blockchain.platform.utils.ChartUtils;
import com.blockchain.platform.utils.IntUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * tv控制器,k线(前端插件使用类似轮询的方式udf)
 *
 * @author zhangye
 **/
@RestController
@RequestMapping("/chart")
public class ChartController extends BaseController {

    /**
     * 图表类接口
     */
    @Resource
    private IChartService chartService;


    @Autowired
    KlineService klineService;

    /**
     * 图标配置
     *
     * @return 当前图表配置
     */
    @RequestMapping("/config")
    public Map<String, Object> config() {
        return ChartUtils.config();
    }

    /**
     * 获取当前市场交易对
     *
     * @param symbol (?symbols=EMC-USDT)
     * @return
     */
    @RequestMapping("/symbols")
    public TvConfigVO symbols(@RequestParam String symbol) {
        TvConfigVO vo = ChartUtils.symbol();

        // 获取小数位数
        Map<String, CoinEntity> config = redisPlugin.get(RedisConst.PLATFORM_COIN_CONFIG);
        // 当前市场 小数位数
        String market = BizUtils.market(symbol);
        // 存在数据
        if (MapUtil.isNotEmpty(config) && config.containsKey(market)) {
            List<String> digits = Arrays.asList(StrUtil.split(config.get(market).getDecLength(), StrUtil.COMMA));
            Integer digit = IntUtils.toInt(digits.get(0));
            vo.setPricescale(IntUtils.toInt(NumberUtil.pow(10, digit)));
        }
        // 当前交易对
        vo.setName(symbol);
        vo.setDescription(symbol);
        vo.setTicker(symbol);
        vo.setCurrency_code(BizUtils.market(symbol));
        return vo;
    }

    /**
     * 时间戳
     *
     * @param request
     * @return
     */
    @RequestMapping("/time")
    public Long time(HttpServletRequest request) {
        return DateUtil.currentSeconds();
    }

    /**
     * 获取history
     * 这里前端轮询请求，影响性能(?symbol=EMC-USDT&resolution=480&from=1564277484&to=1564565484)
     */
    @RequestMapping("/history")
    public TvChartVO history(@RequestParam Map<String, Object> map) {

        TvChartVO data = TvChartVO.builder().s(TvConst.NO_DATA).build();
        try {
            TvDTO dto = null;
            try {
                dto = BeanUtil.mapToBean(map, TvDTO.class, Boolean.TRUE);
            } catch (Exception ex) {
                return data;
            }
            // 货币配置
            Map<String, MarketCoinEntity> dict = redisPlugin.get(RedisConst.PLATFORM_PAIRS_CONFIG);


            String resolution= convertResolution(dto);

            // 当前交易对时间
            MarketCoinEntity entity = dict.get(dto.getSymbol());
            if (ObjectUtil.isEmpty(entity)) {
                return data;
            }

            // 开盘时间
            Long open = BizUtils.timestamp( entity.getCreateTime());

            // 时间区间
            String vrs = dto.getResolution().toUpperCase();
            Long cycle = BizUtils.getCycle( vrs);
            // 如果 传入时间 > 交易所上线时间
            // 8h之后出数据
            if(dto.getTo() < open + cycle) {
                return data;
            }

            List<KLinesVo> kLinesVoList = klineService.kline(dto.getSymbol(), BizUtils.getTvFrom(dto, open) * 1000, dto.getTo() * 1000, dto.getLimit(),resolution);

            List<Number[]> klineNumbers = kLinesVoList.stream().map(kline -> new Number[]{kline.getTime(), kline.getOpenPrice(),
                    kline.getHighestPrice(), kline.getLowestPrice(),
                    kline.getClosePrice(), kline.getVolume()}).collect(Collectors.toList());

            data.setS(TvConst.OK);
            // 数据
            data.setList(klineNumbers);

            return data;
        } catch (Exception ex) {
            return data;
        }
    }

    private String convertResolution(TvDTO dto) {
        String resolution=dto.getResolution().toLowerCase();
        if(resolution.matches("\\d+$")){
            resolution=resolution+"m";
        }else if(resolution.endsWith("d")){

        }else if(resolution.endsWith("w")){

        }

        return resolution;
    }

}
