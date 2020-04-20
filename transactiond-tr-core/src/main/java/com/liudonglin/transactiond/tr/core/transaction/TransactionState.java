package com.liudonglin.transactiond.tr.core.transaction;

public enum TransactionState {

    /**
     * 回滚
     */
    Exception(0),

    /**
     * 提交
     */
    Success(1);

    private final int value;

    TransactionState(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static TransactionState valueOf(int value) {
        switch (value) {
            case 0: return Exception;
            case 1: return Success;
            default: return null;
        }
    }
}
