package com.liudonglin.transactiond.tr.core.context;

import com.liudonglin.transactiond.tr.core.transaction.TransactionModel;

import java.util.HashSet;
import java.util.Set;

public class TxContext {

    public TxContext(String groupId, boolean dtxStart) {
        this.groupId = groupId;
        this.dtxStart = dtxStart;
    }

    /**
     * 事务组锁
     */
    private Object lock = new Object();

    /**
     * 事务组ID
     */
    private String groupId;

    /**
     * 分布式事务发起方
     */
    private boolean dtxStart;

    /**
     * 事务创建时间
     */
    private long createTime = System.currentTimeMillis();

    /**
     * 上下文内分布式事务类型
     */
    private Set<TransactionModel> transactionModels = new HashSet<>(6);

    public Object getLock() {
        return lock;
    }

    public String getGroupId() {
        return groupId;
    }

    public boolean isDtxStart() {
        return dtxStart;
    }

    public long getCreateTime() {
        return createTime;
    }

    public Set<TransactionModel> getTransactionModels() {
        return transactionModels;
    }
}
