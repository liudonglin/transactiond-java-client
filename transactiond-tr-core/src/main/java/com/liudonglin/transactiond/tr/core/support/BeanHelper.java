package com.liudonglin.transactiond.tr.core.support;

import com.liudonglin.transactiond.tr.core.transaction.DTXTransactionController;
import com.liudonglin.transactiond.tr.core.transaction.TransactionCleanService;
import com.liudonglin.transactiond.tr.core.transaction.TransactionModel;
import com.liudonglin.transactiond.tr.core.transaction.TransactionResourceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BeanHelper {

    private final ApplicationContext spring;

    private final static String TransactionResourceProxyFormat = "transactionResourceProxy_%s";

    /**
     * Transaction Clean Service
     * %s: transaction type
     */
    private static final String TransactionCleanServiceNameFormat = "transactionCleanService_%s";

    /**
     * DTXTransactionController bean 名称格式
     * control_%s_%s
     * transaction:前缀 %s:事务类型（lcn,tcc,txc） %s:事务状态(create,join)
     */
    private static final String ControllerBeanNameFormat = "transactionController_%s_%s";

    @Autowired
    public BeanHelper(ApplicationContext spring) {
        this.spring = spring;
    }

    public TransactionResourceProxy loadTransactionResourceProxy(TransactionModel model) {
        String name = String.format(TransactionResourceProxyFormat, model.message());
        return spring.getBean(name, TransactionResourceProxy.class);
    }

    public DTXTransactionController loadDTXLocalControl(TransactionModel transactionModel, DTXPropagationState lcnTransactionState) {
        String beanName = String.format(ControllerBeanNameFormat, transactionModel.message(), lcnTransactionState.getCode());
        return spring.getBean(beanName, DTXTransactionController.class);
    }

    public TransactionCleanService loadTransactionCleanService(TransactionModel transactionModel) {
        return spring.getBean(String.format(TransactionCleanServiceNameFormat, transactionModel.message()), TransactionCleanService.class);
    }
}
