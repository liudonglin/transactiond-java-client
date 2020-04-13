package com.liudonglin.transactiond.tr.core.transaction;

public interface TransactionCleanService {

    /**
     * 事务清理业务
     *
     * @param groupId  groupId
     * @param state    事务状态 1 提交 0 回滚
     */
    void clear(String groupId, int state);
}
