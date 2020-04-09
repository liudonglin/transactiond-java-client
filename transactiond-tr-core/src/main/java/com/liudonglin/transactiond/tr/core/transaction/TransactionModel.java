package com.liudonglin.transactiond.tr.core.transaction;

public enum TransactionModel {

    /**
     * 如果没有,就新建一个事务; 如果有，就加入当前事务
     */
    TCC(1,"tcc"),
    /**
     * 如果没有，就以非事务方式执行；如果有，就使用当前事务
     */
    LCN(2,"lcn");

    private final int value;
    /**
     * 信息
     */
    private final String message;

    private TransactionModel(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int value() {
        return this.value;
    }

    public String message() {
        return message;
    }
}
