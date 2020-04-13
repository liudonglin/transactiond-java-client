package com.liudonglin.transactiond.tr.core.transaction.tcc;

import com.liudonglin.transactiond.tr.core.transaction.TransactionResourceProxy;
import org.springframework.stereotype.Service;

import java.sql.Connection;

@Service(value = "transactionResourceProxy_tcc")
public class TccTransactionResourceProxy implements TransactionResourceProxy {

    @Override
    public Connection proxyConnection(Connection scon) throws Throwable {
        return scon;
    }

}
