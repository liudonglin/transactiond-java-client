package com.liudonglin.transactiond.tr.core.transaction.tcc;

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

@Service(value = "transactionController_tcc_join")
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

    public void preBusinessCode(DTXTransactionInfo info) throws TransactionException {

        // 缓存TCC事务信息，如果有必要
        try {
            globalContext.tccTransactionInfo(info.getUnitId(), () -> CreateTransactionController.prepareTccInfo(info))
                    .setMethodParameter(info.getTransactionMethodInfo().getArgumentValues());
        } catch (Throwable throwable) {
            throw new TransactionException(throwable);
        }
    }

    public void onBusinessCodeError(DTXTransactionInfo info, Throwable throwable) {
        try {
            transactionControlTemplate.clearGroup(info.getGroupId(), TransactionState.Exception,info.getUnitId());
        } catch (Exception e) {
            log.error("tcc > clean transaction error.", e);
        }
    }

    public void onBusinessCodeSuccess(DTXTransactionInfo info, Object result) throws TransactionException {
        DTXLocalContext.cur().setSysTransactionState(TransactionState.Success);
        // join DTX group
        transactionControlTemplate.joinGroup(info.getGroupId(),info.getUnitId(),info.getTransactionModel());
    }

}
