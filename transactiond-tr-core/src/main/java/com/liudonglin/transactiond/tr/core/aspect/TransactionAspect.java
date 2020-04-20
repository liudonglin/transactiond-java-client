package com.liudonglin.transactiond.tr.core.aspect;

import com.liudonglin.transactiond.tr.core.config.DTXClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TransactionAspect implements Ordered {

    private final DTXClientConfig clientConfig;

    private final TransactionInterceptorware interceptor;

    @Autowired
    public TransactionAspect(DTXClientConfig clientConfig, TransactionInterceptorware interceptor) {
        this.clientConfig = clientConfig;
        this.interceptor = interceptor;
    }

    /**
     * DTC Aspect (Type of LCN)
     */
    @Pointcut("@annotation(com.liudonglin.transactiond.tr.core.annotation.LcnTransaction)")
    public void lcnTransactionPointcut() {
    }

    /**
     * DTC Aspect (Type of TCC)
     */
    @Pointcut("@annotation(com.liudonglin.transactiond.tr.core.annotation.TccTransaction)")
    public void tccTransactionPointcut() {
    }

    @Around("lcnTransactionPointcut() && !tccTransactionPointcut()")
    public Object runWithLcnTransaction(ProceedingJoinPoint point) throws Throwable {
        //将执行分布式事务的方法放在TransactionMethodInfo对象里面
        TransactionMethodInfo dtxInfo = TransactionMethodInfo.getFromCache(point);
        //调用方法，正式开启(或继续，这里取决于是否是事务发起方)分布式事务
        return interceptor.runTransaction(dtxInfo, point::proceed);
    }

    @Around("tccTransactionPointcut() && !lcnTransactionPointcut()")
    public Object runWithTccTransaction(ProceedingJoinPoint point) throws Throwable {
        TransactionMethodInfo dtxInfo = TransactionMethodInfo.getFromCache(point);
        return interceptor.runTransaction(dtxInfo, point::proceed);
    }

    @Override
    public int getOrder() {
        return clientConfig.getDtxAspectOrder();
    }
}
