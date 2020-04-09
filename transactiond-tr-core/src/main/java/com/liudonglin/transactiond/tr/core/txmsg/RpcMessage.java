package com.liudonglin.transactiond.tr.core.txmsg;

import lombok.Data;

@Data
public class RpcMessage {

    /**
     * 请求动作
     */
    private ActionType action;

    /**
     * 事务组
     */
    private String groupId;

}
