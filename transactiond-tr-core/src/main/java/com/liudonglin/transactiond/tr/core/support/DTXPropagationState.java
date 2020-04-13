package com.liudonglin.transactiond.tr.core.support;

public enum DTXPropagationState {

    /**
     * 创建DTX
     */
    CREATE("create"),

    /**
     * 加入DTX
     */
    JOIN("join"),

    /**
     * 静默加入DTX
     */
    SILENT_JOIN("default"),

    /**
     * 不参与DTX
     */
    NON("non");


    private String code;


    DTXPropagationState(String code) {
        this.code = code;
    }


    public String getCode() {
        return code;
    }

    public boolean isIgnored() {
        return this.equals(NON);
    }
}
