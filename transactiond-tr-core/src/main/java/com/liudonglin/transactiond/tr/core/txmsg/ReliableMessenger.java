package com.liudonglin.transactiond.tr.core.txmsg;

import com.liudonglin.transactiond.tr.core.transaction.TransactionModel;
import com.liudonglin.transactiond.tr.core.transaction.TransactionState;

import java.util.HashSet;
import java.util.Set;

public interface ReliableMessenger {

    /**
     * 创建事务组
     *
     * @param groupId groupId
     */
    void createGroup(String groupId) ;

    /**
     * 加入事务组
     *
     * @param groupId           groupId
     * @param unitId            事务单元标识
     * @param model             事务模式
     */
    void joinGroup(String groupId, String unitId, TransactionModel model);

    /**
     * 通知事务组
     * @param groupId       groupId
     * @param state         事务状态
     */
    void notifyGroup(String groupId, TransactionState state);
}
