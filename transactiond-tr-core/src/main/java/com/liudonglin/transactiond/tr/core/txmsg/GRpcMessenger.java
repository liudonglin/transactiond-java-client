package com.liudonglin.transactiond.tr.core.txmsg;

import com.liudonglin.transactiond.tr.core.config.DTXClientConfig;
import com.liudonglin.transactiond.tr.core.transaction.TransactionModel;
import com.liudonglin.transactiond.tr.core.transaction.TransactionState;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import txmsg.*;

@Component
@Slf4j
public class GRpcMessenger implements ReliableMessenger  {

    private final DTXClientConfig clientConfig;

    private ManageServiceGrpc.ManageServiceBlockingStub manageServiceClient;

    private ManagedChannel managedChannel;

    public GRpcMessenger(DTXClientConfig clientConfig,
                         ManagedChannel managedChannel) {
        this.clientConfig = clientConfig;
        this.managedChannel = managedChannel;
        manageServiceClient = ManageServiceGrpc.newBlockingStub(managedChannel);
    }

    @Override
    public void createGroup(String groupId) {
        CreateGroupMessage msg = CreateGroupMessage
                .newBuilder()
                .setGroupId(groupId)
                .build();
        manageServiceClient.createGroup(msg);
    }

    @Override
    public void joinGroup(String groupId, String unitId, TransactionModel model) {
        JoinGroupMessage msg = JoinGroupMessage
                .newBuilder()
                .setGroupId(groupId)
                .setUnitId(unitId)
                .setModel(txmsg.TransactionModel.valueOf(model.value()))
                .build();
        manageServiceClient.joinGroup(msg);
    }

    @Override
    public void notifyGroup(String groupId, TransactionState state) {
        NotifyGroupMessage msg = NotifyGroupMessage
                .newBuilder()
                .setGroupId(groupId)
                .setState(txmsg.TransactionState.valueOf(state.value()))
                .build();
        manageServiceClient.notifyGroup(msg);
    }
}