package com.liudonglin.transactiond.tr.core.transaction.lcn;

import com.liudonglin.transactiond.tr.core.context.DTXGlobalContext;
import com.liudonglin.transactiond.tr.core.context.DTXLocalContext;
import com.liudonglin.transactiond.tr.core.transaction.TransactionResourceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;

@Service(value = "transactionResourceProxy_lcn")
public class LcnTransactionResourceProxy implements TransactionResourceProxy {

    private final DTXGlobalContext globalContext;

    @Autowired
    public LcnTransactionResourceProxy(DTXGlobalContext globalContext) {
        this.globalContext = globalContext;
    }

    @Override
    public Connection proxyConnection(Connection scon) throws Throwable {
        String groupId = DTXLocalContext.cur().getGroupId();
        try {
            return globalContext.getLcnConnection(groupId);
        } catch (RuntimeException e) {
            LcnConnectionProxy lcnConnectionProxy = new LcnConnectionProxy(scon);
            globalContext.setLcnConnection(groupId, lcnConnectionProxy);
            lcnConnectionProxy.setAutoCommit(false);
            return lcnConnectionProxy;
        }
    }
}
