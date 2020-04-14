package com.liudonglin.transactiond.tr.core.transaction;

import com.liudonglin.transactiond.tr.core.context.DTXContext;
import com.liudonglin.transactiond.tr.core.context.DTXGlobalContext;
import com.liudonglin.transactiond.tr.core.support.BeanHelper;
import com.liudonglin.transactiond.tr.core.txmsg.ReliableMessenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionControlTemplate {

    private BeanHelper beanHelper;

    private final ReliableMessenger reliableMessenger;

    private final DTXGlobalContext globalContext;

    @Autowired
    public TransactionControlTemplate(BeanHelper beanHelper,
                                      ReliableMessenger reliableMessenger,
                                      DTXGlobalContext globalContext) {
        this.beanHelper = beanHelper;
        this.reliableMessenger = reliableMessenger;
        this.globalContext = globalContext;
    }

    public void createGroup(String groupId) {
        reliableMessenger.createGroup(groupId);
    }

    public void joinGroup(String groupId, String unitId, TransactionModel model) {
        reliableMessenger.joinGroup(groupId, unitId, model);
    }

    public void notifyGroup(String groupId, TransactionState state) {
        reliableMessenger.notifyGroup(groupId, state);
    }

    /**
     * 清理事务时调用
     *
     * @param groupId groupId
     */
    public void clearGroup(String groupId, TransactionState state) {
        DTXContext txContext = globalContext.txContext(groupId);
        TransactionCleanService clean = beanHelper.loadTransactionCleanService(txContext.getTransactionModel());
        clean.clear(groupId, state);
    }

}
