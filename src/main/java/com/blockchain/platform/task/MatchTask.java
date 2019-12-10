package com.blockchain.platform.task;

import com.blockchain.platform.dictionary.DictionaryFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
@Component
public class MatchTask implements Ordered, ApplicationRunner {
    private static MatchTask ourInstance = new MatchTask();

    public static MatchTask getInstance() {
        return ourInstance;
    }

    /**
     * 工厂类
     */
    @Resource
    private DictionaryFactory factory;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        Thread.sleep(1000);
        factory.startMatchThread();
        System.out.println("撮合已经启动");
    }

    @Override
    public int getOrder() {
        return 1000;
    }
}
