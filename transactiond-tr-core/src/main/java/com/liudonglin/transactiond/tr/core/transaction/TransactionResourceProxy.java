package com.liudonglin.transactiond.tr.core.transaction;

import java.sql.Connection;

public interface TransactionResourceProxy {
    /**
     * 获取资源连接
     *
     * @param scon Connection提供者
     * @return  Connection Connection
     * @throws Throwable Throwable
     */
    Connection proxyConnection(Connection scon) throws Throwable;
}
