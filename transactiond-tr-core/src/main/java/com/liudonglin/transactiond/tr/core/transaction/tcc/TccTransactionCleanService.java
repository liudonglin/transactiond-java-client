package com.liudonglin.transactiond.tr.core.transaction.tcc;

import com.liudonglin.transactiond.tr.core.context.DTXGlobalContext;
import com.liudonglin.transactiond.tr.core.context.DTXLocalContext;
import com.liudonglin.transactiond.tr.core.support.TransactionException;
import com.liudonglin.transactiond.tr.core.tracing.TracingContext;
import com.liudonglin.transactiond.tr.core.transaction.TccTransactionInfo;
import com.liudonglin.transactiond.tr.core.transaction.TransactionCleanService;
import com.liudonglin.transactiond.tr.core.transaction.TransactionState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
@Service(value = "transactionCleanService_tcc")
public class TccTransactionCleanService implements TransactionCleanService{

    private final DTXGlobalContext globalContext;

    private final ApplicationContext applicationContext;

    public TccTransactionCleanService(DTXGlobalContext globalContext, ApplicationContext applicationContext) {
        this.globalContext = globalContext;
        this.applicationContext = applicationContext;
    }

    @Override
    public void clear(String groupId, TransactionState state, String unitId) throws TransactionException {
        Method exeMethod;
        boolean shouldDestroy = !TracingContext.tracing().hasGroup();

        try {
            TccTransactionInfo tccInfo = globalContext.tccTransactionInfo(unitId, null);
            Object object = applicationContext.getBean(tccInfo.getExecuteClass());
            DTXLocalContext.getOrNew().setGroupId(groupId);
            DTXLocalContext.cur().setUnitId(unitId);
            exeMethod = tccInfo.getExecuteClass().getMethod(
                    state == TransactionState.Success ? tccInfo.getConfirmMethod() : tccInfo.getCancelMethod(),
                    tccInfo.getMethodTypeParameter());
            try {
                exeMethod.invoke(object, tccInfo.getMethodParameter());
                log.debug("User confirm/cancel logic over.");
            } catch (Throwable e) {
                log.error("Tcc clean error.", e);
            }
        } catch (Throwable e) {
            throw new TransactionException(e.getMessage());
        } finally {

            if (shouldDestroy) {
                TracingContext.tracing().destroy();
            }
        }
    }

}
