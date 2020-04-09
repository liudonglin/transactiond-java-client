package com.liudonglin.transactiond.tr.core.transaction.lcn;

import com.liudonglin.transactiond.tr.core.context.DTXGlobalContext;
import com.liudonglin.transactiond.tr.core.support.TransactionException;
import com.liudonglin.transactiond.tr.core.transaction.DTXLocalControl;
import com.liudonglin.transactiond.tr.core.transaction.DTXTransactionInfo;
import com.liudonglin.transactiond.tr.core.txmsg.ReliableMessenger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "control_lcn_starting")
@Slf4j
public class LcnStartingTransaction implements DTXLocalControl {

    //private final TransactionControlTemplate transactionControlTemplate;

    private final DTXGlobalContext globalContext;

    private final ReliableMessenger reliableMessenger;

    @Autowired
    public LcnStartingTransaction(DTXGlobalContext globalContext, ReliableMessenger reliableMessenger) {
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
    }

    @Override
    public void onBusinessCodeSuccess(DTXTransactionInfo info, Object result) {
    }

    @Override
    public void postBusinessCode(DTXTransactionInfo info) {

    }
}
