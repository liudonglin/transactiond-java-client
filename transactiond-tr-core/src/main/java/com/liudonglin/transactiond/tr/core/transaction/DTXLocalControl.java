package com.liudonglin.transactiond.tr.core.transaction;

import com.liudonglin.transactiond.tr.core.support.TransactionException;

public interface DTXLocalControl {

    /**
     * 业务代码执行前
     *
     * @param info info
     * @throws  TransactionException TransactionException
     */
    default void preBusinessCode(DTXTransactionInfo info) throws TransactionException {

    }

    /**
     * 执行业务代码
     *
     * @param info info
     * @return  Object Object
     * @throws Throwable Throwable
     */
    default Object doBusinessCode(DTXTransactionInfo info) throws Throwable {
        return info.getBusinessCallback().call();
    }


    /**
     * 业务代码执行失败
     *
     * @param info info
     * @param throwable throwable
     * @throws TransactionException TransactionException
     */
    default void onBusinessCodeError(DTXTransactionInfo info, Throwable throwable) throws TransactionException {

    }

    /**
     * 业务代码执行成功
     *
     * @param info info
     * @param result result
     * @throws TransactionException TransactionException
     */
    default void onBusinessCodeSuccess(DTXTransactionInfo info, Object result) throws TransactionException {

    }

    /**
     * 清场
     *
     * @param info info
     */
    default void postBusinessCode(DTXTransactionInfo info) {

    }
}
