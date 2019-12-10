package com.blockchain.platform.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.blockchain.platform.config.SMSConfig;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.pojo.dto.SmsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 短信服务工具类
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-05-16 10:26 AM
 **/
@Component
public class SMSUtils {

    /**
     * 短信配置信息
     */
    @Resource
    private SMSConfig config;

    private static final String SIGN = "sign";

    final static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    /**
     * 发送验证码
     * @param type
     * @param values
     */
    public void sendMessage(String type, String mobile ,String[] values) {
        // 短信模板
        String content = StrUtil.EMPTY;

        Map<String, Object> param = new HashMap<>();

        switch ( type) {
            case BizConst.SMS_TYPE_REGISTER :
                // 注册模板
                content = config.getRegisterContent();
                // 注册参数
                param = config.getRegisterParam();
                break;
            case BizConst.SMS_TYPE_ADDRESS :
                content = config.getAddressContent();
                param = config.getAddressParam();
                break;
            case BizConst.SMS_TYPE_CIPHER :
                // 资金模板
                content = config.getCipherContent();
                // 资金参数
                param = config.getRegisterParam();
                break;
            case BizConst.SMS_TYPE_BIND :
                //绑定
                content = config.getBindContent();
                param = config.getBindParam();
                break;
            case BizConst.CHANGE_CIPHER:
            {
                content = config.getChangeCipher();
                break;

            }
            default:
                // 注册模板
                content = config.getRegisterContent();
                // 注册参数
                param = config.getRegisterParam();

                break;
        }
        final String c = content;

        final Map<String, Object> m = param;
        // 发送短信
        cachedThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                send( mobile, c, m, values);
            }
        });
    }

    /**
     * 实际发送短信
     * @param mobile
     * @param content
     * @param map
     * @param values
     */
    public void send(String mobile, String content, Map<String, Object> map,String [] values) {

        if(map!=null) {
            // 开始封装 实际发送信息
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                // 是否包含
                if (StrUtil.containsAny(content, entry.getKey())) {
                    Integer idx = IntUtils.toInt(entry.getValue());
                    if (values.length > idx) { // 可以执行替换
                        content = StrUtil.replace(content, entry.getKey(), values[idx], Boolean.TRUE);
                    }
                }
            }
        }
        // 标识
        content = StrUtil.replace(content, SIGN, config.getSign(), Boolean.TRUE);
        // 短信参数
        SmsDTO dto = SmsDTO.builder()
                            .appid( config.getAppId())
                            .to( mobile)
                            .content( content)
                            .signature( config.getAppKey()).build();


        Map<String, Object> json = BeanUtil.beanToMap( dto);

        HttpResponse response = HttpRequest.post( config.getDomain())
                                            .header(Header.CONTENT_TYPE, AppConst.RESPONSE_CONTENT_TYPE)
                                            .body( JSONUtil.toJsonStr( json)).execute();
        System.out.println("短信发送：" + response.body());
        if ( response.body().indexOf("success") == -1) {
            throw new UnsupportedOperationException( response.body());
        }
    }
}
