package com.liudonglin.transactiond.tr.core.support;

import com.liudonglin.transactiond.tr.core.transaction.DTXLocalControl;
import com.liudonglin.transactiond.tr.core.transaction.TransactionModel;
import com.liudonglin.transactiond.tr.core.transaction.TransactionResourceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BeanHelper {

    private final ApplicationContext spring;

    private final static String TransactionResourceProxyFormat = "%sTransactionResourceProxy";

    /**
     * DTXLocalControl bean 名称格式
     * control_%s_%s
     * transaction:前缀 %s:事务类型（lcn,tcc,txc） %s:事务状态(starting,running)
     */
    private static final String CONTROL_BEAN_NAME_FORMAT = "control_%s_%s";

    @Autowired
    public BeanHelper(ApplicationContext spring) {
        this.spring = spring;
    }

    public TransactionResourceProxy loadTransactionResourceProxy(TransactionModel model) {
        String name = String.format(TransactionResourceProxyFormat, model.message());
        return spring.getBean(name, TransactionResourceProxy.class);
    }

    public DTXLocalControl loadDTXLocalControl(TransactionModel transactionModel, DTXPropagationState lcnTransactionState) {
        String beanName = String.format(CONTROL_BEAN_NAME_FORMAT, transactionModel.message(), lcnTransactionState.getCode());
        return spring.getBean(beanName, DTXLocalControl.class);
    }

}
