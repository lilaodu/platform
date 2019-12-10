package com.blockchain.platform.plugins.spring;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 容器插件
 *
 * @author David.Li
 * @version 1.0
 * @create 2019-08-30 11:19 AM
 **/
@Component
public class SpringPlugin implements ApplicationContextAware {

    /**
     * 当前容器对象
     */
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if ( ObjectUtil.isEmpty( this.applicationContext)) {
            SpringPlugin.applicationContext = applicationContext;
        }
    }




    /**
     * 根据名称获取 Bean
     * @param name
     * @return
     */
    public static Object getBean(String name){
        return applicationContext.getBean(name);
    }

    /**
     * 通过class获取Bean
     * @param clazz
     * @param <T>
     * @return
     */
    public static  <T> T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name,Class<T> clazz){
        return applicationContext.getBean(name, clazz);
    }
}
