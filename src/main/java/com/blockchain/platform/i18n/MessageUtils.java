package com.blockchain.platform.i18n;

import cn.hutool.core.util.StrUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * i18n消息工具类
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-04-23 1:46 PM
 **/
@Component
public class MessageUtils {

    /**
     * 消息数据
     */
    @Resource
    private MessageSource messageSource;


    /**
     * 获取配置新
     * @param msgKey
     * @param lang 语言
     * @return
     */
    public String get(String msgKey, String lang) {
        try {
            return messageSource.getMessage(msgKey, null, getLocale( lang));
        } catch (Exception e){
            // 当无法获取 语言文字时 不抛出异常
            return msgKey;
        }
    }

    /**
     * 获取语言标识
     * @param lang
     * @return
     */
    private Locale getLocale( String lang) {
        Locale locale = Locale.SIMPLIFIED_CHINESE;

        if (StrUtil.isNotEmpty( lang)) {
            locale = Locale.forLanguageTag( lang);
        }
        return locale;
    }

}
