package com.dicka.microservicecatalog.utils;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Component
@Slf4j
public class ContextCopyHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy{

//    public ContextCopyHystrixConcurrencyStrategy(){
//        HystrixPlugins.getInstance()
//                .registerConcurrencyStrategy(this);
//    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable){
        return new MyCallable(callable, MyThreadLocalsHolder.getCorrelationId());
    }

    public static class MyCallable<T> implements Callable<T> {

        private final Callable<T> actual;
        private final String correlationId;

        public MyCallable(Callable<T> actual, String correlationId){
            this.actual = actual;
            this.correlationId = correlationId;
        }

        @Override
        public T call() throws Exception {
            log.info("-------call()---------");
            MyThreadLocalsHolder.setCorelationId(correlationId);
            try{
                return actual.call();
            }finally {
                MyThreadLocalsHolder.setCorelationId(correlationId);
            }
        }
    }
}
