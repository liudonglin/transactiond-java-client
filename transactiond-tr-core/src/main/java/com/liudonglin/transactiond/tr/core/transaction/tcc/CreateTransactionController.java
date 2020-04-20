package com.liudonglin.transactiond.tr.core.transaction.tcc;

import com.liudonglin.transactiond.tr.core.annotation.TccTransaction;
import com.liudonglin.transactiond.tr.core.context.DTXGlobalContext;
import com.liudonglin.transactiond.tr.core.context.DTXLocalContext;
import com.liudonglin.transactiond.tr.core.support.TransactionException;
import com.liudonglin.transactiond.tr.core.transaction.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

@Service(value = "transactionController_tcc_create")
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

    static TccTransactionInfo prepareTccInfo(DTXTransactionInfo info) throws TransactionException {
        Method method = info.getTransactionMethodInfo().getMethod();
        TccTransaction tccTransaction = method.getAnnotation(TccTransaction.class);
        if (tccTransaction == null) {
            throw new TransactionException("TCC type need @TccTransaction in " + method.getName());
        }
        String cancelMethod = tccTransaction.cancelMethod();
        String confirmMethod = tccTransaction.confirmMethod();
        Class<?> executeClass = tccTransaction.executeClass();
        if (StringUtils.isEmpty(tccTransaction.cancelMethod())) {
            cancelMethod = "cancel" + StringUtils.capitalize(method.getName());
        }
        if (StringUtils.isEmpty(tccTransaction.confirmMethod())) {
            confirmMethod = "confirm" + StringUtils.capitalize(method.getName());
        }
        if (Void.class.isAssignableFrom(executeClass)) {
            executeClass = info.getTransactionMethodInfo().getTargetClazz();
        }

        TccTransactionInfo tccInfo = new TccTransactionInfo();
        tccInfo.setExecuteClass(executeClass);
        tccInfo.setCancelMethod(cancelMethod);
        tccInfo.setConfirmMethod(confirmMethod);
        tccInfo.setMethodParameter(info.getTransactionMethodInfo().getArgumentValues());
        tccInfo.setMethodTypeParameter(info.getTransactionMethodInfo().getParameterTypes());

        return tccInfo;
    }

    @Override
    public void preBusinessCode(DTXTransactionInfo info) throws TransactionException {
// cache tcc info
        try {
            globalContext.tccTransactionInfo(info.getUnitId(), () -> prepareTccInfo(info))
                    .setMethodParameter(info.getTransactionMethodInfo().getArgumentValues());
        } catch (Throwable throwable) {
            throw new TransactionException(throwable);
        }

        // create DTX group
        transactionControlTemplate.createGroup(info.getGroupId());
    }

    @Override
    public void onBusinessCodeError(DTXTransactionInfo info, Throwable throwable) {
        DTXLocalContext.cur().setSysTransactionState(TransactionState.Exception);
    }

    @Override
    public void onBusinessCodeSuccess(DTXTransactionInfo info, Object result) {
        DTXLocalContext.cur().setSysTransactionState(TransactionState.Success);
    }

    /**
     * 事务发起方 自己执行  提交 / 取消 事件
     *
     * @param info info
     */
    @Override
    public void postBusinessCode(DTXTransactionInfo info) {
        this.transactionControlTemplate.notifyGroup(info.getGroupId(),DTXLocalContext.cur().getSysTransactionState());
    }

}
