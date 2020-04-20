package com.liudonglin.transactiond.tr.core.transaction.lcn;

import com.liudonglin.transactiond.tr.core.context.DTXGlobalContext;
import com.liudonglin.transactiond.tr.core.transaction.TransactionCleanService;
import com.liudonglin.transactiond.tr.core.transaction.TransactionState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service(value = "transactionCleanService_lcn")
public class LcnTransactionCleanService implements TransactionCleanService {

    private final DTXGlobalContext globalContext;

    @Autowired
    public LcnTransactionCleanService(DTXGlobalContext globalContext) {
        this.globalContext = globalContext;
    }

    @Override
    public void clear(String groupId, TransactionState state, String unitId) {
        try {
            LcnConnectionProxy connectionProxy = globalContext.getLcnConnection(groupId);
            connectionProxy.notify(state);
            // todo notify exception
        } catch (Exception e) {
            log.warn("Non lcn connection when clear transaction.");
        }
    }
}

