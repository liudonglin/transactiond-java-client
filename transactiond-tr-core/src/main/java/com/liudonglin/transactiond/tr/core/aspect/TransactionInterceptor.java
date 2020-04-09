package com.liudonglin.transactiond.tr.core.aspect;

import com.liudonglin.transactiond.tr.core.context.DTXGlobalContext;
import com.liudonglin.transactiond.tr.core.context.DTXLocalContext;
import com.liudonglin.transactiond.tr.core.context.TxContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class TransactionInterceptor {

    private final DTXGlobalContext globalContext;

    @Autowired
    public TransactionInterceptor(DTXGlobalContext globalContext) {
        this.globalContext = globalContext;
    }

    //执行分布式事务的核心方法
    public Object runTransaction(DTXInfo dtxInfo, BusinessCallback business) throws Throwable {

        if (Objects.isNull(DTXLocalContext.cur())) {
            DTXLocalContext.getOrNew();
        } else {
            return business.call();
        }

        log.debug("<---- TxLcn start ---->");
        //1.拿到当前模块的事务上下文和全局事务上下文
        DTXLocalContext dtxLocalContext = DTXLocalContext.getOrNew();
        TxContext txContext;
        // ---------- 保证每个模块在一个DTX下只会有一个TxContext ---------- //
        if (globalContext.hasTxContext()) {
            // 有事务上下文的获取父上下文
            txContext = globalContext.txContext();
            dtxLocalContext.setInGroup(true);
            log.debug("Unit[{}] used parent's TxContext[{}].", dtxInfo.getUnitId(), txContext.getGroupId());
        } else {
            // 没有的开启本地事务上下文
            txContext = globalContext.startTx();
        }

        // 本地事务调用
        if (Objects.nonNull(dtxLocalContext.getGroupId())) {
            dtxLocalContext.setDestroy(false);
        }

        dtxLocalContext.setUnitId(dtxInfo.getUnitId());
        dtxLocalContext.setGroupId(txContext.getGroupId());
        dtxLocalContext.setTransactionModel(dtxInfo.getTransactionModel());



        return null;
    }

}
