package com.blockchain.platform.utils;

import java.util.HashMap;
import java.util.Map;
import com.blockchain.platform.pojo.vo.TvConfigVO;

/**
 * tradeview工具类
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-05-11 10:03 PM
 **/
public class ChartUtils {

    /**
     * 默认
     */
    public static final String [] DEFAULT_SUP_RES = {"1","5","15","30","60","480","1D","1W"};


    /**
     * chart 配置
     * @return
     */
    public static Map<String, Object> config(){
        Map<String, Object> data = new HashMap<>();
        // 服务器是否反馈样式信息
        data.put("supports_marks", false);
        // 是否获取服务器时间戳
        data.put("supports_time", true);
        // 是否获取服务器查询，支持搜索
        data.put("supports_search", true);

        data.put("supports_group_request", false);
        // 默认时间轴线
        data.put("supported_resolutions", DEFAULT_SUP_RES);
        return data;
    }

    /**
     * 无数据返回
     * @return
     */
    public static Map<String, Object> noData(){
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("s", "no_data");
        return data;
    }

    /**
     * symbol对象
     * @return
     */
    public static TvConfigVO symbol() {
        TvConfigVO vo = TvConfigVO.builder()
                            .timezone( "Asia/Shanghai")
                            .session( "24x7")
                            .has_intraday( true)
                            .supported_resolutions( DEFAULT_SUP_RES)
                            .type( "stock")
                            .volume_precision( 8)
                            .pointvalue( 1)
                            .minmov( 1).minmov2( 0)
                            .has_no_volume( false)
                            .pricescale( 100000000).build();
        return vo;
    }

}
