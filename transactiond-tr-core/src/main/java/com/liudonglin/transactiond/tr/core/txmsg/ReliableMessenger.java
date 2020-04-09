package com.liudonglin.transactiond.tr.core.txmsg;

import java.util.HashSet;
import java.util.Set;

public interface ReliableMessenger {

    /**
     * 创建事务组
     *
     * @param groupId groupId
     */
    void createGroup(String groupId) ;

}
