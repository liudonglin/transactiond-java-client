package com.liudonglin.transactiond.tr.core.txmsg;

public enum RpcResponseState {

    /**
     * 执行成功
     */
    Success(0),

    /**
     * 执行失败
     */
    Fail(1);

    private final int value;


    RpcResponseState(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
