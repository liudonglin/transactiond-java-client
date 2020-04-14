package com.liudonglin.transactiond.tr.core.transaction;

public enum TransactionState {

    /**
     * 回滚
     */
    Rollback(0),

    /**
     * 提交
     */
    Commit(1);

    private final int value;

    TransactionState(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static TransactionState valueOf(int value) {
        switch (value) {
            case 0: return Rollback;
            case 1: return Commit;
            default: return null;
        }
    }
}
