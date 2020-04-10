package com.liudonglin.transactiond.tr.core.txmsg;

import com.liudonglin.transactiond.tr.core.config.DTXClientConfig;
import txmsg.ActionType;
import txmsg.ManageServiceGrpc;
import txmsg.RpcMessage;
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
        RpcMessage msg = RpcMessage.newBuilder()
                .setActionType(ActionType.CreateGroup)
                .setGroupId(groupId).build();
        RpcMessage resp = manageServiceClient.sendMessage(msg);
    }
}