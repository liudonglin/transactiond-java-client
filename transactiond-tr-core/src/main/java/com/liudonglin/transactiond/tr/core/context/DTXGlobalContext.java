package com.liudonglin.transactiond.tr.core.context;

import com.liudonglin.transactiond.tr.core.config.DTXClientConfig;
import com.liudonglin.transactiond.tr.core.support.TransactionException;
import com.liudonglin.transactiond.tr.core.tracing.TracingContext;
import com.liudonglin.transactiond.tr.core.support.Supplier;
import com.liudonglin.transactiond.tr.core.transaction.TccTransactionInfo;
import com.liudonglin.transactiond.tr.core.transaction.lcn.LcnConnectionProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class DTXGlobalContext {

    private final DTXClientConfig clientConfig;

    private final DTXContextCache attachmentCache;

    private final static String txContextKeySuffix = ".dtx";

    @Autowired
    public DTXGlobalContext(DTXClientConfig clientConfig, DTXContextCache attachmentCache) {
        this.clientConfig = clientConfig;
        this.attachmentCache = attachmentCache;
    }

    public void setLcnConnection(String groupId, LcnConnectionProxy connectionProxy) {
        attachmentCache.attach(groupId, LcnConnectionProxy.class.getName(), connectionProxy);
    }

    public LcnConnectionProxy getLcnConnection(String groupId) throws RuntimeException {
        if (attachmentCache.containsKey(groupId, LcnConnectionProxy.class.getName())) {
            return attachmentCache.attachment(groupId, LcnConnectionProxy.class.getName());
        }
        throw new RuntimeException("non exists lcn connection");
    }

    public DTXContext txContext(String groupId) {
        return attachmentCache.attachment(groupId + txContextKeySuffix);
    }

    public DTXContext txContext() {
        return txContext(TracingContext.tracing().groupId());
    }

    public boolean hasTxContext() {
        return TracingContext.tracing().hasGroup() && txContext(TracingContext.tracing().groupId()) != null;
    }

    public DTXContext startTx() {
        DTXContext txContext = new DTXContext();
        // 事务发起方判断
        txContext.setDtxStart(!TracingContext.tracing().hasGroup());
        if (txContext.isDtxStart()) {
            TracingContext.tracing().beginTransactionGroup();
        }
        txContext.setGroupId(TracingContext.tracing().groupId());
        String txContextKey = txContext.getGroupId() + ".dtx";
        attachmentCache.attach(txContextKey, txContext);
        log.debug("Start TxContext[{}]", txContext.getGroupId());
        return txContext;
    }

    public TccTransactionInfo tccTransactionInfo(String unitId, Supplier<TccTransactionInfo, TransactionException> supplier)
            throws TransactionException {
        String unitTransactionInfoKey = unitId + ".tcc.transaction";
        if (Objects.isNull(supplier)) {
            return attachmentCache.attachment(unitTransactionInfoKey);
        }

        if (attachmentCache.containsKey(unitTransactionInfoKey)) {
            return attachmentCache.attachment(unitTransactionInfoKey);
        }

        TccTransactionInfo tccTransactionInfo = supplier.get();
        attachmentCache.attach(unitTransactionInfoKey, tccTransactionInfo);
        return tccTransactionInfo;
    }

}
