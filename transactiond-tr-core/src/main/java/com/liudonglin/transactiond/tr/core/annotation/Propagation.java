package com.liudonglin.transactiond.tr.core.annotation;

import org.springframework.util.StringUtils;

public enum Propagation {
    /**
     * 如果没有,就新建一个事务; 如果有，就加入当前事务
     */
    REQUIRED(0,"Required"),
    /**
     * 如果没有，就以非事务方式执行；如果有，就使用当前事务
     */
    SUPPORTS(1,"Supports");

    /**
     * 如果没有，就抛出异常;如果有，就使用当前事务。
     */
    //MANDATORY(2,"Mandatory"),

    /**
     * 有没有都新建事务，如果原来有，就将原来的挂起。
     */
    //REQUIRES_NEW(3,"RequiresNew");

    private final int value;
    /**
     * 信息
     */
    private final String message;

    private Propagation(int value,String message) {
        this.value = value;
        this.message = message;
    }

    public static Propagation parser(String code) {
        if (StringUtils.isEmpty(code)) {
            return REQUIRED;
        }
        code = code.toUpperCase();

        for (Propagation value : values()) {
            if (code.equals(value.message().toUpperCase())) {
                return value;
            }
        }
        return REQUIRED;
    }

    public int value() {
        return this.value;
    }

    public String message() {
        return message;
    }
}
