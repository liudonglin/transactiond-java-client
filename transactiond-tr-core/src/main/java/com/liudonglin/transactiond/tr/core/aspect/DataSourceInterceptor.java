package com.liudonglin.transactiond.tr.core.aspect;

import com.liudonglin.transactiond.tr.core.support.BeanHelper;
import com.liudonglin.transactiond.tr.core.transaction.TransactionModel;
import com.liudonglin.transactiond.tr.core.context.DTXLocalContext;
import com.liudonglin.transactiond.tr.core.transaction.TransactionResourceProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
@Slf4j
public class DataSourceInterceptor {

    private final BeanHelper beanHelper;

    @Autowired
    public DataSourceInterceptor(BeanHelper beanHelper) {
        this.beanHelper = beanHelper;
    }

    public Object getConnection(Connection scon) throws Throwable {
        DTXLocalContext dtxLocalContext = DTXLocalContext.cur();
        if (dtxLocalContext != null) {
            TransactionModel model = dtxLocalContext.getTransactionModel();
            TransactionResourceProxy resourceProxy = beanHelper.loadTransactionResourceProxy(model);
            Connection pcon = resourceProxy.proxyConnection(scon);
            log.debug("proxy a sql connection: {}.", pcon);
            return pcon;
        }
        return scon;
    }
}
