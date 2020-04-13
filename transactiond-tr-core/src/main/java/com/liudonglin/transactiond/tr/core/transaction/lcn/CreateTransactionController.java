package com.liudonglin.transactiond.tr.core.transaction.lcn;

import com.liudonglin.transactiond.tr.core.context.DTXGlobalContext;
import com.liudonglin.transactiond.tr.core.context.DTXLocalContext;
import com.liudonglin.transactiond.tr.core.support.TransactionException;
import com.liudonglin.transactiond.tr.core.transaction.DTXTransactionController;
import com.liudonglin.transactiond.tr.core.transaction.DTXTransactionInfo;
import com.liudonglin.transactiond.tr.core.transaction.TransactionState;
import com.liudonglin.transactiond.tr.core.txmsg.ReliableMessenger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "transactionController_lcn_create")
@Slf4j
public class CreateTransactionController implements DTXTransactionController {

    private final DTXGlobalContext globalContext;

    private final ReliableMessenger reliableMessenger;

    @Autowired
    public CreateTransactionController(DTXGlobalContext globalContext, ReliableMessenger reliableMessenger) {
        this.globalContext = globalContext;
        this.reliableMessenger = reliableMessenger;
    }

    @Override
    public void preBusinessCode(DTXTransactionInfo info) throws TransactionException {
        // 创建事务组消息
        reliableMessenger.createGroup(info.getGroupId());
    }

    @Override
    public void onBusinessCodeError(DTXTransactionInfo info, Throwable throwable) {
        DTXLocalContext.cur().setSysTransactionState(TransactionState.Rollback);
    }

    @Override
    public void onBusinessCodeSuccess(DTXTransactionInfo info, Object result) {
        DTXLocalContext.cur().setSysTransactionState(TransactionState.Commit);
    }

    @Override
    public void postBusinessCode(DTXTransactionInfo info) {
        reliableMessenger.notifyGroup(info.getGroupId(),DTXLocalContext.cur().getSysTransactionState());
    }
}
