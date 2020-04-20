package com.liudonglin.transactiond.tr.core.transaction;

import lombok.Data;

@Data
public class TccTransactionInfo {


    /**
     * Tcc 事务 提交/回滚 执行类
     */
    private Class<?> executeClass;


    /**
     * 回滚方法名称
     */
    private String cancelMethod;


    /**
     * 提交名称
     */
    private String confirmMethod;


    /**
     * 参数
     */
    private Object[] methodParameter;


    /**
     * 参数类型
     */
    private Class[] methodTypeParameter;

}