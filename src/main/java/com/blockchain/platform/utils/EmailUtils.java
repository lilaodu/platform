package com.blockchain.platform.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.blockchain.platform.config.EmailConfig;
import com.blockchain.platform.config.SMSConfig;
import com.blockchain.platform.constant.BizConst;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 邮件发送工具类
 *
 * @author ml
 * @version 1.0
 * @create 2019-04-29 7:55 PM
 **/
@Component
public class EmailUtils {

    @Resource
    private EmailConfig config;

    @Resource
    private SMSConfig smsConfig;


    final static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    /**
     * 实际发送
     * @param type
     * @param email
     * @param title
     * @param values
     */
    public void sendMessage(String type, String email, String title, String [] values) {
        // 短信模板
        String content = StrUtil.EMPTY;

        Map<String, Object> param = new  HashMap<>();

        switch ( type) {
            case BizConst.SMS_TYPE_REGISTER :
                // 注册模板
                content = smsConfig.getRegisterContent();
                // 注册参数
                param = smsConfig.getRegisterParam();
                break;
            case BizConst.SMS_TYPE_ADDRESS :
                content = smsConfig.getAddressContent();
                param = smsConfig.getAddressParam();
                break;
            case BizConst.SMS_TYPE_CIPHER :
                // 资金模板
                content = smsConfig.getCipherContent();
                // 资金参数
                param = smsConfig.getRegisterParam();
                break;
            case BizConst.CHANGE_CIPHER:
            {
                content = smsConfig.getChangeCipher();
                break;

            }

            case BizConst.SMS_TYPE_BIND :
                //绑定
                content = smsConfig.getBindContent();
                param = smsConfig.getBindParam();
                break;
            case BizConst.SMS_TYPE_WITHDRAW :
                //模板
                content = smsConfig.getWithdrawContent();
                //参数
                param = smsConfig.getWithdrawParam();
                break;

            default:
                // 注册模板
                content = smsConfig.getRegisterContent();
                // 注册参数
                param = smsConfig.getRegisterParam();

                break;
        }

        // 开始封装 实际发送信息
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            // 是否包含
            if ( StrUtil.containsAny(content, entry.getKey()) ) {
                Integer idx = IntUtils.toInt( entry.getValue());
                if ( values.length > idx) { // 可以执行替换
                    content = StrUtil.replace( content, entry.getKey(), values[ idx], Boolean.TRUE);
                }
            }
        }

        content = StrUtil.subAfter(content, "】", Boolean.FALSE);


        title = StrUtil.concat( Boolean.FALSE, "【", smsConfig.getSign(),"】", title);

        final String c = content;

        final String t = title;

        cachedThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                submail( email, c, t);
            }
        });
    }


    /**
     * 赛迪邮件发送
     * @param email
     * @param content
     * @param title
     */
    public void submail(String email, String content, String title) {

        Map<String, Object> map = MapUtil.newHashMap();
        map.put("from", config.getFrom());
        map.put("to", email);
        map.put("appid", config.getAppId());
        map.put("subject", title);
        map.put("signature", config.getAppKey());
        map.put("text", content);

        String body = HttpUtil.post( config.getUrl(), map);
        System.out.println("邮箱发送：" + body);
        if ( !StrUtil.containsAny( body, "success")) {
            throw new UnsupportedOperationException( body);
        }
    }

    /**
     * 发送邮件
     * @param email
     * @param content
     * @param title
     * @return
     */
    public void send(String email, String content, String title){
//        try {
//            //邮件发送实体
//            Map<String,Object> map = new HashMap<>();
//            //url
//            String webUrl = config.getUrl();
//            //apiUser
//            map.put( EmailConst.EMAIL_API_USER, config.getUsername());
//            //apiKey
//            map.put( EmailConst.EMAIL_API_KEY,  config.getPassword());
//            //发件人邮箱地址
//            map.put( EmailConst.EMAIL_FROM, EmailConst.EMAIL_OFFICIAL);
//            //发件人
//            map.put( EmailConst.EMAIL_FROM_NAME, EmailConst.EMAIL_NAME);
//            //收件人
//            map.put( EmailConst.EMAIL_TO, email);
//            //邮件标题
//            map.put( EmailConst.EMAIL_SUBJECT, title);
//            //邮件正文
//            map.put( EmailConst.EMAIL_HTML, content);
//            String response = HttpUtil.post( webUrl, map);
//            if ( !StrUtil.containsAny( response.toUpperCase(), HttpStatus.OK.getReasonPhrase())) {
//                throw new UnsupportedOperationException();
//            }
//        }catch ( Exception ex){
//            ExUtils.error( ex, LocaleKey.SYS_OPERATE_FAILED);
//        }
    }
}
