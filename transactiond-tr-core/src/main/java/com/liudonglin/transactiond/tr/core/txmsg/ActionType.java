package com.liudonglin.transactiond.tr.core.txmsg;

public enum ActionType {

    CreateGroup(1);

    private final int value;

    private ActionType(int value) {
        this.value = value;
    }

}
