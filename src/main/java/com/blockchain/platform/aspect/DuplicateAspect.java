package com.blockchain.platform.aspect;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.blockchain.platform.annotation.DuplicateVerify;
import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.i18n.LocaleKey;
import com.blockchain.platform.plugins.redis.RedisPlugin;
import com.blockchain.platform.utils.ExUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 验证重复提交
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-14 11:41 AM
 **/
@Aspect
@Component
public class DuplicateAspect {

    /**
     * 重复提交缓存记录
     */
    public static final Long DUPLICATE_VERIFY_TIMES = 3L;

    /**
     * redis 工具
     */
    @Resource
    private RedisPlugin redisUtils;

    /**
     * 定义切面位置
     */
    @Pointcut("@annotation(com.blockchain.platform.annotation.DuplicateVerify)")
    private void DuplicateAspect(){
    }

    /**
     * 切面执行
     * @param point
     * @return
     */
    @Around("DuplicateAspect()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //对象
        Object target = point.getTarget();

        Signature signature = point.getSignature();

        // 参数类型
        MethodSignature ms = (MethodSignature) signature;


        // 当前执行方法
        Method method = target.getClass().getMethod( signature.getName(), ms.getMethod().getParameterTypes());

        // 是否存在标签 防止意外
        if ( method.isAnnotationPresent( DuplicateVerify.class)) {

            DuplicateVerify verify = method.getAnnotation( DuplicateVerify.class);

            // 请求对象
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();


            HttpServletRequest request = attributes.getRequest();

            //token
            String token = request.getHeader( AppConst.TOKEN);

            String key = StrUtil.concat( Boolean.TRUE, verify.method().getValue() ,token);
            // 强制验证 字段
            String value = redisUtils.get( key);

            if ( verify.isForce()) {
                // 缓存值
                StringBuffer buffer = new StringBuffer();
                for (String filed : verify.filed()) {

                    buffer.append( getFieldValue(point, filed));

                    buffer.append( StrUtil.UNDERLINE);
                }
                if ( StrUtil.isNotEmpty( value) && StrUtil.equals( value, buffer.toString())) {
                    throw ExUtils.error( LocaleKey.SYS_OPERATE_TOO_FREQUENT);
                }
                redisUtils.set(key, value, DUPLICATE_VERIFY_TIMES);
            } else {
                if ( StrUtil.isNotEmpty( value)) {
                    throw ExUtils.error( LocaleKey.SYS_OPERATE_TOO_FREQUENT);
                }
                redisUtils.set(key, IdUtil.simpleUUID(), DUPLICATE_VERIFY_TIMES);
            }
        }
        return point.proceed();
    }


    /**
     * 获取参数
     * @param point
     * @param field
     * @return
     */
    private static Object getFieldValue(JoinPoint point, String field) {
        Object value = null;
        Object[] args = point.getArgs();
        for (int idx = 0;idx < args.length; idx ++) {
            Object param = args[ idx];
            if ( ObjectUtil.isNotEmpty( value)) {
                break;
            }
            value = BeanUtil.getFieldValue( param, field);
        }
        return value;
    }

}
