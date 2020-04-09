package com.liudonglin.transactiond.tr.core.aspect;

import com.liudonglin.transactiond.tr.core.context.DTXGlobalContext;
import com.liudonglin.transactiond.tr.core.context.DTXLocalContext;
import com.liudonglin.transactiond.tr.core.context.DTXContext;
import com.liudonglin.transactiond.tr.core.transaction.DTXServiceExecutor;
import com.liudonglin.transactiond.tr.core.transaction.DTXTransactionInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class TransactionInterceptorware {

    private final DTXServiceExecutor transactionServiceExecutor;

    private final DTXGlobalContext globalContext;

    @Autowired
    public TransactionInterceptorware(DTXServiceExecutor transactionServiceExecutor, DTXGlobalContext globalContext) {
        this.transactionServiceExecutor = transactionServiceExecutor;
        this.globalContext = globalContext;
    }

    //执行分布式事务的核心方法
    public Object runTransaction(TransactionMethodInfo methodInfo, BusinessCallback business) throws Throwable {

        if (Objects.isNull(DTXLocalContext.cur())) {
            DTXLocalContext.getOrNew();
        } else {
            return business.call();
        }

        log.debug("<---- dtx start ---->");
        //1.拿到当前模块的事务上下文和全局事务上下文
        DTXLocalContext dtxLocalContext = DTXLocalContext.getOrNew();
        DTXContext dtxContext;
        // ---------- 保证每个模块在一个DTX下只会有一个TxContext ---------- //
        if (globalContext.hasTxContext()) {
            // 有事务上下文的获取父上下文
            dtxContext = globalContext.txContext();
            dtxLocalContext.setInGroup(true);
            log.debug("Unit[{}] used parent's TxContext[{}].", methodInfo.getUnitId(), dtxContext.getGroupId());
        } else {
            // 没有的开启本地事务上下文
            dtxContext = globalContext.startTx();
        }

        // 本地事务调用
        if (Objects.nonNull(dtxLocalContext.getGroupId())) {
            dtxLocalContext.setDestroy(false);
        }

        dtxLocalContext.setUnitId(methodInfo.getUnitId());
        dtxLocalContext.setGroupId(dtxContext.getGroupId());
        dtxLocalContext.setTransactionModel(methodInfo.getTransactionModel());

        DTXTransactionInfo info = new DTXTransactionInfo(dtxContext.isDtxStart(),
                dtxContext.getGroupId(),
                methodInfo,
                business);

        //LCN事务处理器
        try {
            return transactionServiceExecutor.transactionRunning(info);
        } finally {

        }
    }

}
