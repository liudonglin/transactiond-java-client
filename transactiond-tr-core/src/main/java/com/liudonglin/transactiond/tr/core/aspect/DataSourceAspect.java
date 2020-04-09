package com.liudonglin.transactiond.tr.core.aspect;

import com.liudonglin.transactiond.tr.core.config.DTXClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Aspect
@Component
@Slf4j
public class DataSourceAspect implements Ordered {

    private final DTXClientConfig dtxClientConfig;
    private final DataSourceInterceptor interceptor;

    public DataSourceAspect(DTXClientConfig dtxClientConfig, DataSourceInterceptor dtxResourceWeaver) {
        this.dtxClientConfig = dtxClientConfig;
        this.interceptor = dtxResourceWeaver;
    }

    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        return interceptor.getConnection((Connection) point.proceed());
    }

    @Override
    public int getOrder() {
        return dtxClientConfig.getResourceOrder();
    }

}
