package com.blockchain.platform.utils;


import com.blockchain.platform.pojo.vo.KLinesVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @create 2019-02-25 12:37
 **/
public class KlineUtil {


    public static final String min_1 = "1m";
    public static final String min_5 = "5m";
    public static final String min_15 = "15m";
    public static final String min_30 = "30m";
    public static final String hour_1 = "60m";
    public static final String hour_4 = "240m";

    public static final String hour_8 = "480m";
    public static final String day_1 = "1d";
    public static final String week_1 = "1w";

    public static final Map<String, Long> klineTypes = new ConcurrentHashMap<>();
    public static final List<String> types = new ArrayList<>();
    public final Map<String, KLinesVo> KLines = new ConcurrentHashMap<>();
    public BigDecimal yesterdayPrice = BigDecimal.ZERO;
    public BigDecimal yesterdayVol = BigDecimal.ZERO;
     public BigDecimal currentMarketPrice=BigDecimal.ZERO;
    String pair;

    static {
        types.add(min_1);
        types.add(min_5);
        types.add(min_15);
        types.add(min_30);
        types.add(hour_1);
        types.add(hour_8);
        types.add(day_1);
        types.add(hour_4);

        types.add(week_1);
        klineTypes.put(min_1, 60 * 1000L);
        klineTypes.put(min_5, 5 * 60 * 1000L);
        klineTypes.put(min_15, 15 * 60 * 1000L);
        klineTypes.put(min_30, 30 * 60 * 1000L);
        klineTypes.put(hour_1, 60 * 60 * 1000L);
        klineTypes.put(hour_8, 8 * 60 * 60 * 1000L);
        klineTypes.put(day_1, 24 * 60 * 60 * 1000L);
        klineTypes.put(week_1, 7 * 24 * 60 * 60 * 1000L);
        klineTypes.put(hour_4, 4 * 60 * 60 * 1000L);

    }

    public KlineUtil(String pair) {
        this.pair = pair;
        KLines.put(min_1, new KLinesVo());
        KLines.put(min_5, new KLinesVo());
        KLines.put(min_15, new KLinesVo());
        KLines.put(min_30, new KLinesVo());
        KLines.put(hour_1, new KLinesVo());
        KLines.put(hour_4, new KLinesVo());

        KLines.put(hour_8, new KLinesVo());
        KLines.put(day_1, new KLinesVo());
        KLines.put(week_1, new KLinesVo());

    }


    /**
     * 放入 k 线
     *
     * @param price
     * @param number
     * @param timestamp
     */
    public KLinesVo put(BigDecimal price, BigDecimal number, long timestamp) {
        KLinesVo isNeedSave = null;
        currentMarketPrice=price;
        KLinesVo KLine = KLines.get(min_1);
        if (isContainer(timestamp, min_1)) {
            if (price.compareTo(KLine.getHighestPrice()) > 0) KLine.setHighestPrice(price);
            if (price.compareTo(KLine.getLowestPrice()) < 0) KLine.setLowestPrice(price);
            KLine.setVolume(KLine.getVolume().add(number));
            KLine.setClosePrice(price);
        } else {
            isNeedSave = KLine; // 需要保存
            KLine = new KLinesVo();
            KLine.setOpenPrice(price);
            KLine.setHighestPrice(price);
            KLine.setLowestPrice(price);
            KLine.setClosePrice(price);
            KLine.setVolume(number);
            KLine.setTime(getTimestampByType(timestamp, min_1));
            KLines.put(min_1, KLine);
        }
        for (String type : types) {
            merge(KLine, type, number);
        }
        if (isNeedSave != null && isNeedSave.getTime() == 0) {
            isNeedSave = KLine;
        }
        return isNeedSave;
    }

    /**
     * 将1分钟线合并到其他线
     */
    private void merge(KLinesVo min1, String type, BigDecimal number) {
        if (isContainer(min1.getTime(), type)) {
            if (min1.getHighestPrice().compareTo(KLines.get(type).getHighestPrice()) > 0)
                KLines.get(type).setHighestPrice(min1.getHighestPrice());
            if (min1.getLowestPrice().compareTo(KLines.get(type).getLowestPrice()) < 0)
                KLines.get(type).setLowestPrice(min1.getLowestPrice());
            KLines.get(type).setVolume(KLines.get(type).getVolume().add(number));
            KLines.get(type).setClosePrice(min1.getClosePrice());
        } else {
            KLinesVo KLine = new KLinesVo();
            KLine.setOpenPrice(min1.getOpenPrice());
            KLine.setHighestPrice(min1.getHighestPrice());
            KLine.setLowestPrice(min1.getLowestPrice());
            KLine.setClosePrice(min1.getClosePrice());
            KLine.setVolume(min1.getVolume());
            KLine.setTime(getTimestampByType(min1.getTime(), type));
            if (type.equals(day_1)) {
                // 缓存昨日的价格
                KLinesVo yesterday = KLines.get(type);
                if (yesterday.getClosePrice().compareTo(BigDecimal.ZERO) == 0) {
                    this.yesterdayPrice = KLine.getClosePrice();
                } else {
                    this.yesterdayPrice = yesterday.getClosePrice();
                    this.yesterdayVol = yesterday.getVolume();
                }
            }
            KLines.put(type, KLine);
        }
    }

    private boolean isContainer(long timestamp, String type) {
        return timestamp - KLines.get(type).getTime() < klineTypes.get(type);
    }

    private long getTimestampByType(long timestamp, String type) {
        return timestamp - timestamp % klineTypes.get(type);
    }


}
