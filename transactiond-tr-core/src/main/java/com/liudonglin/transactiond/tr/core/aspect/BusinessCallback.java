package com.liudonglin.transactiond.tr.core.aspect;

@FunctionalInterface
public interface BusinessCallback {

    /**
     * call user business.
     *
     * @return business result
     * @throws Throwable business exception
     */
    Object call() throws Throwable;
}
