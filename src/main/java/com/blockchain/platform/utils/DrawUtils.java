package com.blockchain.platform.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.blockchain.platform.pojo.dto.LuckDrawDTO;
import com.blockchain.platform.pojo.vo.LuckDrawConfigVO;

import java.util.*;

/**
 * 抽奖工具类
 *
 * @author ml
 * @version 1.0
 * @create 2019-08-15 9:50 PM
 **/
public class DrawUtils {

    /**
     * 用户抽奖的方法
     * @param map  抽奖活动奖项概率
     * @param dto   //抽奖活动详情
     */
    public static final Integer draw(Map<Integer, Double> map, LuckDrawDTO dto){
        //抽奖的随机范围
        Integer[] values = new Integer[]{1,2,3,4,5,6,7,8};
        List<Integer> number = CollUtil.toList( values);
        //处理抽奖范围
        if( !IntUtils.isZero( dto.getFirst())){  //一等奖被抽中
            //随机数 移除掉 一等奖
           BizUtils.removePrize( number, 8);
        }
        //判断最近的40单是否出现了1，2，3，4等奖
        if( IntUtils.isZero( dto.getFirst()) && !IntUtils.isZero( dto.getPrize())){
            //移除掉 1，2，3，4等奖
            BizUtils.removePrize( number,  5,6,7,8);
        }else if( !IntUtils.isZero( dto.getPrize())){
            //移除掉 2，3，4等奖
            BizUtils.removePrize( number,  5,6,7);
        }
        //获取抽奖范围的最大与最小值
        Integer max = Collections.max( number);  //最大值
        Integer min = Collections.min( number);  //最小值
        //产生随机数
        int prize = new Random().nextInt(max - min + 1) + min;
        //获取抽奖活动的奖项的概率
        if( map.containsKey( prize)){
            //抽奖
            prize = probability( number, map);
        }
        //谢谢参与
        return  prize;
    }


    /**
     * 根据概率生成随机数
     * @param config
     * @return
     */
    public static int probability(List<Integer> prizes,Map<Integer, Double> config) {
        // 产生随机数
        double random = Math.random();

        Set<Integer> list = config.keySet();

        // 抽奖最小值
        int min = Collections.min( list);

        int fi = -1;
        for (Map.Entry<Integer, Double> entry : config.entrySet()) {

            double rate = 0d;

            double rate0 = deploy( rate, min, entry.getKey() - 1, config);

            double rate1 = deploy( rate ,min, entry.getKey(), config);

            if ( random >= rate0 && random <= rate1) {
                fi = entry.getKey();
                break;
            }
        }
        return fi < 0 || !prizes.contains( fi) ? min : fi;
    }

    /**
     * 获取概率
     * @param min
     * @param cur
     * @param config
     * @return
     */
    public static double deploy(double db, int min, int cur, Map<Integer, Double> config) {
        if ( cur >= min) {
            db += config.get( cur);
            if ( cur -- > min) {
                return deploy(db, min, cur, config);
            }
        }
        return db;
    }


    public static void main(String[] args){
        //测试中奖概率
        //抽奖的随机范围
        Integer[] values = new Integer[]{1,2,3,4,5,6,7,8};
        List<Integer> number = CollUtil.toList( values);
        //获取抽奖范围的最大与最小值
        Integer max = Collections.max( number);  //最大值
        Integer min = Collections.min( number);  //最小值
        //奖项
        Map<Integer,Double> map = new HashMap<>();
        map.put(1, 0.5); // 谢谢参与
        map.put(2, 0.22); // 1 USDT
        map.put(3, 0.13);  // 2 USDT
        map.put(4, 0.09); // 5 USDT
        map.put(5, 0.03); // 10 USDT
        map.put(6, 0.011); // 20 USDT
        map.put(7, 0.01); // 30 USDT
        map.put(8, 0.001); // 50 USDT
        //打印结果
        Map<Integer,Integer> result = new HashMap<>();
        for( int idx = 0 ; idx < 100000; idx ++){

            int  k = probability(number, map);

            int num = !result.containsKey( k) ? 1 : result.get( k) + 1;

            result.put(k, num);
        }
    }

}
