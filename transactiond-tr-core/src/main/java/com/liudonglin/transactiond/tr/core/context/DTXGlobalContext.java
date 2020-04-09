package com.liudonglin.transactiond.tr.core.context;

import com.liudonglin.transactiond.tr.core.config.DTXClientConfig;
import com.liudonglin.transactiond.tr.core.transaction.lcn.LcnConnectionProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public TxContext txContext(String groupId) {
        return attachmentCache.attachment(groupId + txContextKeySuffix);
    }

    public TxContext txContext() {
        //todo 远程请求tm
        return txContext("1");
    }

    public boolean hasTxContext() {
        //todo 远程请求tm
        return false;
    }

    public TxContext startTx() {
        //todo 远程请求tm,事务发起方判断
        Boolean hasGroup = false;
        //todo 远程请求tm
        String groupId = "1";

        TxContext txContext = new TxContext(groupId,hasGroup);

        if (txContext.isDtxStart()) {
            //todo 远程创建事务组
            //TracingContext.tracing().beginTransactionGroup();
        }

        String txContextKey = txContext.getGroupId() + txContextKeySuffix;
        attachmentCache.attach(txContextKey, txContext);
        log.debug("Start TxContext[{}]", txContext.getGroupId());
        return txContext;
    }

}
