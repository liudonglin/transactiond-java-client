package com.liudonglin.transactiond.tr.core.support;

import com.liudonglin.transactiond.tr.core.transaction.TransactionModel;
import com.liudonglin.transactiond.tr.core.transaction.TransactionResourceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BeanHelper {

    private final ApplicationContext spring;

    private final static String TransactionResourceProxyFormat = "%sTransactionResourceProxy";

    @Autowired
    public BeanHelper(ApplicationContext spring) {
        this.spring = spring;
    }

    public TransactionResourceProxy loadTransactionResourceProxy(TransactionModel model) {
        String name = String.format(TransactionResourceProxyFormat, model.message());
        return spring.getBean(name, TransactionResourceProxy.class);
    }

}
