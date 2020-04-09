package com.liudonglin.transactiond.tr.core.transaction;

import com.liudonglin.transactiond.tr.core.annotation.Propagation;
import com.liudonglin.transactiond.tr.core.aspect.BusinessCallback;
import com.liudonglin.transactiond.tr.core.aspect.TransactionMethodInfo;

public class DTXTransactionInfo {

    /**
     * 事务发起方
     */
    private boolean transactionStart;

    /**
     * 事务组标识
     */
    private String groupId;

    /**
     * 事务切面信息
     */
    private TransactionMethodInfo transactionMethodInfo;

    /**
     * 业务执行器
     */
    private BusinessCallback businessCallback;

    public DTXTransactionInfo(boolean transactionStart,
                              String groupId,
                              TransactionMethodInfo transactionMethodInfo,
                              BusinessCallback businessCallback) {
        this.transactionStart = transactionStart;
        this.groupId = groupId;
        this.transactionMethodInfo = transactionMethodInfo;
        this.businessCallback = businessCallback;
    }

    public boolean isTransactionStart() {
        return transactionStart;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getUnitId() {
        return transactionMethodInfo.getUnitId();
    }

    public TransactionMethodInfo getTransactionMethodInfo() {
        return transactionMethodInfo;
    }

    public BusinessCallback getBusinessCallback() {
        return businessCallback;
    }

    /**
     * 事务传播
     */
    public Propagation getPropagation() {
        return transactionMethodInfo.getPropagation();
    }

    /**
     * 事务类型
     */
    public TransactionModel getTransactionModel() {
        return transactionMethodInfo.getTransactionModel();
    }

}
