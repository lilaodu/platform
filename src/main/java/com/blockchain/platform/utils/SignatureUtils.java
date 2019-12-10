package com.blockchain.platform.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;

import java.net.URLEncoder;
import java.util.*;

/**
 * 签名工具
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-08-05 6:10 PM
 **/
public class SignatureUtils {


    interface Const {

        /**
         * &符号
         */
        String CHAR_ALSO = "&";


        /**
         * 字符等号
         */
        String CHAR_EQUAL = "=";


        /**
         * 加密sign 拼接API密钥
         */
        String CHAR_KEY = "key";
    }


    /**
     * 签名
     * @param value
     * @param secret
     * @return
     */
    public static String signature(Object value, String secret) throws Exception{

        Map map = BeanUtil.beanToMap( value);


        List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>( map.entrySet());



        LinkedHashMap<String, Object> params = new LinkedHashMap<>();


        // 对数据进行排序 参数名ASCII码从小到大排序（字典序)
        Collections.sort(infoIds, new Comparator<Map.Entry<String, Object>>() {
            @Override
            public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                // 参数名区分大小写
                return o1.getKey().compareTo( o2.getKey());
            }
        });

        // 排序 后 字符串
        StringBuffer buffer = formatBizQueryParam( params, false);
        // 添加密钥排序
        buffer.append( Const.CHAR_ALSO)
                .append( Const.CHAR_KEY)
                .append( Const.CHAR_EQUAL)
                .append( secret);

        // md5 加密
        return SecureUtil.md5( buffer.toString()).toUpperCase();
    }


    /**
     * 参数排序
     * @param map 参数
     * @param encode 是否解密
     * @return
     */
    public static StringBuffer formatBizQueryParam(Map<String, Object> map, boolean encode) throws Exception {
        StringBuffer buffer = new StringBuffer();
        try {
            List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>( map.entrySet());
            // 防止排序错误 重新排序
            Collections.sort(infoIds, new Comparator<Map.Entry<String, Object>>() {
                @Override
                public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                    return o1.getKey().compareTo( o2.getKey());
                }
            });



            for (int idx = 0;idx < infoIds.size(); idx ++) {
                Map.Entry<String, Object> item = infoIds.get( idx);
                // value 为空同样不处理 区分大小写
                if( !ObjectUtil.isEmpty( item.getValue())){
                    buffer.append( item.getKey())
                            .append( Const.CHAR_EQUAL)
                            .append(encode ? URLEncoder.encode(StrUtil.toString( item.getValue()), CharsetUtil.UTF_8)
                                    : StrUtil.toString( item.getValue()))
                            .append( idx < infoIds.size() - 1 ? Const.CHAR_ALSO : StrUtil.EMPTY);
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
        return buffer;
    }



}
