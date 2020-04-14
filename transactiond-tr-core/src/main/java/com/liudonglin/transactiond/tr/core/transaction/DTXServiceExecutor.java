package com.liudonglin.transactiond.tr.core.transaction;

import com.liudonglin.transactiond.tr.core.context.DTXGlobalContext;
import com.liudonglin.transactiond.tr.core.support.BeanHelper;
import com.liudonglin.transactiond.tr.core.support.DTXPropagationResolver;
import com.liudonglin.transactiond.tr.core.support.DTXPropagationState;
import com.liudonglin.transactiond.tr.core.support.TransactionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class DTXServiceExecutor {

    private final DTXGlobalContext globalContext;

    private final BeanHelper txLcnBeanHelper;

    private final DTXPropagationResolver propagationResolver;

    @Autowired
    public DTXServiceExecutor(DTXGlobalContext globalContext, BeanHelper txLcnBeanHelper, DTXPropagationResolver propagationResolver) {
        this.globalContext = globalContext;
        this.txLcnBeanHelper = txLcnBeanHelper;
        this.propagationResolver = propagationResolver;
    }

    /**
     * 事务业务执行
     *
     * @param info info
     * @return Object
     * @throws Throwable Throwable
     */
    public Object transactionRunning(DTXTransactionInfo info) throws Throwable {
        // 获取事务传播状态
        DTXPropagationState propagationState = propagationResolver.resolvePropagationState(info);

        // 如果不参与分布式事务立即终止
        if (propagationState.isIgnored()) {
            return info.getBusinessCallback().call();
        }

        // 获取本地分布式事务控制器
        DTXTransactionController dtxLocalControl = txLcnBeanHelper.loadDTXLocalControl(info.getTransactionModel(), propagationState);

        // 织入事务操作
        try{

            dtxLocalControl.preBusinessCode(info);

            // 业务执行前
            //txLogger.txTrace(info.getGroupId(), info.getUnitId(), "pre business code, unit type: {}", transactionType);

            // 执行业务
            Object result = dtxLocalControl.doBusinessCode(info);

            // 业务执行成功
            //txLogger.txTrace(info.getGroupId(), info.getUnitId(), "business success");
            dtxLocalControl.onBusinessCodeSuccess(info, result);
            return result;

        }catch (TransactionException e) {
            //txLogger.error(info.getGroupId(), info.getUnitId(), "before business code error");
            throw e;
        } catch (Throwable e) {
            // 业务执行失败
            //txLogger.error(info.getGroupId(), info.getUnitId(), Transactions.TAG_TRANSACTION,"business code error");
            dtxLocalControl.onBusinessCodeError(info, e);
            throw e;
        } finally {
            // 业务执行完毕
            dtxLocalControl.postBusinessCode(info);
        }

    }
}
