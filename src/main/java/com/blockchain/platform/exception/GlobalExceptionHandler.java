package com.blockchain.platform.exception;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;
import com.blockchain.platform.i18n.MessageUtils;
import com.blockchain.platform.plugins.response.ResponseData;
import com.blockchain.platform.utils.IntUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * 全局异常统一处理
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-14 11:44 AM
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 消息工具
     */
    @Resource
    private MessageUtils messageUtils;

    /**
     * 默认语言
     */
    private static List<String> defaultLang = CollUtil.newArrayList("zh-CN","en-US");

    /**
     * 处理异常
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = Exception.class)
    public ResponseData handlerException(HttpServletRequest request, Exception ex) {
        // 当前用户语言
        String lang = request.getHeader( AppConst.LANG);
        if (StrUtil.isEmpty( lang) || !CollUtil.contains(defaultLang, lang)) {
            lang = defaultLang.get( IntUtils.INT_ZERO.intValue());
        }

        ResponseData data = ResponseData.fail();
        if (ex instanceof BcException) {
            BcException exception = (BcException) ex;

            if (ObjectUtil.isNotEmpty( exception.getValue())) {
                List<Object> list = Arrays.asList( exception.getValue());
                String message =  messageUtils.get( exception.getLocaleKey(), lang);

                for ( int idx = 0; idx < list.size(); idx ++ ) {
                    message = StrUtil.replace( message,
                            StrUtil.concat(Boolean.FALSE, StrUtil.DELIM_START, StrUtil.toString( idx), StrUtil.DELIM_END),
                            StrUtil.toString( list.get( idx)));
                }
                data.setMessage( message);
            } else {
                data.setMessage( messageUtils.get( ex.getMessage(), lang));
            }
            data.setMessage( messageUtils.get( ex.getMessage(), lang));
        } else if(ex instanceof BcLoginException){
            data.setCode( HttpStatus.GONE.value());
            data.setMessage( messageUtils.get( ex.getMessage(), lang));
        } else if(ex instanceof BcAuthException){
            data.setCode( HttpStatus.UNAUTHORIZED.value());
            data.setMessage( messageUtils.get( ex.getMessage(), lang));
        } else {
            ex.printStackTrace();
        }
        return data;
    }

}
