package com.liudonglin.transactiond.tr.core.txmsg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GRpcMessenger implements ReliableMessenger {

    @Override
    public void createGroup(String groupId) {

    }

}
