package com.liudonglin.transactiond.tr.core.txmsg;

import com.liudonglin.transactiond.tr.core.config.DTXClientConfig;
import com.liudonglin.transactiond.tr.core.transaction.TransactionModel;
import txmsg.CreateGroupMessage;
import txmsg.JoinGroupMessage;
import txmsg.ManageServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GRpcMessenger implements ReliableMessenger  {

    private final DTXClientConfig clientConfig;

    private ManageServiceGrpc.ManageServiceBlockingStub manageServiceClient;

    public GRpcMessenger(DTXClientConfig clientConfig) {
        this.clientConfig = clientConfig;

        String managerAddress = clientConfig.getManagerAddress().get(0);
        String host = managerAddress.split(":")[0];
        int port = Integer.parseInt(managerAddress.split(":")[1]);

        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
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
    public void joinGroup(String groupId, String unitId, TransactionModel model, int transactionState) {
        JoinGroupMessage msg = JoinGroupMessage
                .newBuilder()
                .setGroupId(groupId)
                .setUnitId(unitId)
                .setModel(txmsg.TransactionModel.valueOf(model.value()))
                .setState(transactionState)
                .build();
        manageServiceClient.joinGroup(msg);
    }
}