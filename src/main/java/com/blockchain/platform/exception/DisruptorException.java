/**
 * @program: exchange
 * @description: toDo
 * @author: DengWei
 * @create: 2019-05-18
 **/
package com.blockchain.platform.exception;

import com.lmax.disruptor.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: TradeMatchException
 * @description: 交易自定义异常
 **/
@Slf4j
public class DisruptorException implements ExceptionHandler {
    @Override
    public void handleEventException(Throwable throwable, long sequence, Object event) {
        throwable.fillInStackTrace();
        log.error("process data error sequence ==[{}] event==[{}] ,ex ==[{}]", sequence, event.toString(), throwable.getMessage());
    }

    @Override
    public void handleOnStartException(Throwable throwable) {
        log.error("start disruptor error ==[{}]!", throwable.getMessage());
    }

    @Override
    public void handleOnShutdownException(Throwable throwable) {
        log.error("shutdown disruptor error ==[{}]!", throwable.getMessage());
    }
}