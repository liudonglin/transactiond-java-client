package com.liudonglin.transactiond.tr.core.support;

public interface Supplier<T, E extends Throwable> {

    /**
     * 获取 {@code T} 的实例
     *
     * @return T' implementation
     * @throws E ex
     */
    T get() throws E;
}
