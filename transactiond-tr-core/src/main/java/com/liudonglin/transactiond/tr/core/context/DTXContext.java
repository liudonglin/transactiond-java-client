package com.liudonglin.transactiond.tr.core.context;

import com.liudonglin.transactiond.tr.core.transaction.TransactionModel;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class DTXContext {

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
}
