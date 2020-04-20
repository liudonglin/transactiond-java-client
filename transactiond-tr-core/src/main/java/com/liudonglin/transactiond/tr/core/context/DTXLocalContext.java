package com.liudonglin.transactiond.tr.core.context;

import com.liudonglin.transactiond.tr.core.transaction.TransactionModel;
import com.liudonglin.transactiond.tr.core.transaction.TransactionState;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class DTXLocalContext {

    private final static ThreadLocal<DTXLocalContext> currentLocal = new InheritableThreadLocal<>();

    /**
     * 事务模式
     */
    private TransactionModel transactionModel;

    /**
     * 事务组
     */
    private String groupId;

    /**
     * 事务单元
     */
    private String unitId;

    /**
     * 同事务组标识
     */
    private boolean inGroup = false;

    /**
     * 是否需要销毁。什么时候需要？一个请求下来，这个模块有两个Unit被执行，那么被调方是不能销毁的，只能有上层调用方销毁
     */
    private boolean destroy = true;

    /**
     * 系统分布式事务状态
     */
    private TransactionState sysTransactionState = TransactionState.Success;

    /**
     * 获取当前线程变量。不推荐用此方法，会产生NullPointerException
     *
     * @return 当前线程变量
     */
    public static DTXLocalContext cur() {
        return currentLocal.get();
    }

    /**
     * 获取或新建一个线程变量。
     *
     * @return 当前线程变量
     */
    public static DTXLocalContext getOrNew() {
        if (currentLocal.get() == null) {
            currentLocal.set(new DTXLocalContext());
        }
        return currentLocal.get();
    }

}
