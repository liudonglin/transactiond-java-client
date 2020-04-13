package com.liudonglin.transactiond.tr.core.transaction.lcn;

import com.liudonglin.transactiond.tr.core.context.DTXGlobalContext;
import com.liudonglin.transactiond.tr.core.transaction.DTXTransactionController;
import com.liudonglin.transactiond.tr.core.transaction.DTXTransactionInfo;
import com.liudonglin.transactiond.tr.core.txmsg.ReliableMessenger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service(value = "transactionController_lcn_join")
@Slf4j
public class JoinTransactionController implements DTXTransactionController {

    private final DTXGlobalContext globalContext;

    private final ReliableMessenger reliableMessenger;

    public JoinTransactionController(DTXGlobalContext globalContext, ReliableMessenger reliableMessenger) {
        this.globalContext = globalContext;
        this.reliableMessenger = reliableMessenger;
    }

    @Override
    public void preBusinessCode(DTXTransactionInfo info) {
        // lcn type need connection proxy
        //DTXLocalContext.makeProxy();
    }


    @Override
    public void onBusinessCodeError(DTXTransactionInfo info, Throwable throwable) {
        /*try {
            transactionCleanTemplate.clean(info.getGroupId(), info.getUnitId(), info.getTransactionType(), 0);
        } catch (TransactionClearException e) {
            log.error("{} > clean transaction error." , Transactions.LCN);
        }*/
    }


    @Override
    public void onBusinessCodeSuccess(DTXTransactionInfo info, Object result) {
        log.debug("join group: [GroupId: {},Method: {}]" , info.getGroupId(),info.getMethodStr());
        // join DTX group
        reliableMessenger.joinGroup(info.getGroupId(),info.getUnitId(),info.getTransactionModel());
    }
}
