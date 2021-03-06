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

@Service(value = "transactionController_lcn_join")
@Slf4j
public class JoinTransactionController implements DTXTransactionController {

    private final DTXGlobalContext globalContext;

    private final TransactionControlTemplate transactionControlTemplate;

    @Autowired
    public JoinTransactionController(DTXGlobalContext globalContext
            , TransactionControlTemplate transactionControlTemplate) {
        this.globalContext = globalContext;
        this.transactionControlTemplate = transactionControlTemplate;
    }

    @Override
    public void preBusinessCode(DTXTransactionInfo info) {
    }


    @Override
    public void onBusinessCodeError(DTXTransactionInfo info, Throwable throwable) throws TransactionException {
        DTXLocalContext.cur().setSysTransactionState(TransactionState.Exception);

        transactionControlTemplate.clearGroup(info.getGroupId(),TransactionState.Exception,info.getUnitId());
    }


    @Override
    public void onBusinessCodeSuccess(DTXTransactionInfo info, Object result) {
        DTXLocalContext.cur().setSysTransactionState(TransactionState.Success);
        // join DTX group
        transactionControlTemplate.joinGroup(info.getGroupId(),info.getUnitId(),info.getTransactionModel());
    }
}
