package com.liudonglin.transactiond.tr.core.transaction.lcn;

import com.liudonglin.transactiond.tr.core.context.DTXGlobalContext;
import com.liudonglin.transactiond.tr.core.context.DTXLocalContext;
import com.liudonglin.transactiond.tr.core.support.TransactionException;
import com.liudonglin.transactiond.tr.core.transaction.DTXTransactionController;
import com.liudonglin.transactiond.tr.core.transaction.DTXTransactionInfo;
import com.liudonglin.transactiond.tr.core.transaction.TransactionControlTemplate;
import com.liudonglin.transactiond.tr.core.transaction.TransactionState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "transactionController_lcn_create")
@Slf4j
public class CreateTransactionController implements DTXTransactionController {

    private final DTXGlobalContext globalContext;

    private final TransactionControlTemplate transactionControlTemplate;

    @Autowired
    public CreateTransactionController(DTXGlobalContext globalContext,
                                       TransactionControlTemplate transactionControlTemplate) {
        this.globalContext = globalContext;
        this.transactionControlTemplate = transactionControlTemplate;
    }

    @Override
    public void preBusinessCode(DTXTransactionInfo info) throws TransactionException {
        // 创建事务组消息
        this.transactionControlTemplate.createGroup(info.getGroupId());
    }

    @Override
    public void onBusinessCodeError(DTXTransactionInfo info, Throwable throwable) {
        DTXLocalContext.cur().setSysTransactionState(TransactionState.Exception);
    }

    @Override
    public void onBusinessCodeSuccess(DTXTransactionInfo info, Object result) {
        DTXLocalContext.cur().setSysTransactionState(TransactionState.Success);
    }

    @Override
    public void postBusinessCode(DTXTransactionInfo info) throws TransactionException {
        this.transactionControlTemplate.notifyGroup(info.getGroupId(),DTXLocalContext.cur().getSysTransactionState());
        this.transactionControlTemplate.clearGroup(info.getGroupId(),DTXLocalContext.cur().getSysTransactionState(),info.getUnitId());
    }
}
