package com.liudonglin.transactiond.tr.core.txmsg;

import com.liudonglin.transactiond.tr.core.config.DTXClientConfig;
import com.liudonglin.transactiond.tr.core.support.BeanHelper;
import com.liudonglin.transactiond.tr.core.transaction.TransactionCleanService;
import com.liudonglin.transactiond.tr.core.transaction.TransactionModel;
import com.liudonglin.transactiond.tr.core.transaction.TransactionState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import txmsg.*;

@Component
@Slf4j
public class GRpcMessenger implements ReliableMessenger  {

    private final DTXClientConfig clientConfig;

    private ManageServiceGrpc.ManageServiceBlockingStub manageServiceClient;

    private BeanHelper beanHelper;

    public GRpcMessenger(DTXClientConfig clientConfig, BeanHelper beanHelper) {
        this.clientConfig = clientConfig;
        this.beanHelper = beanHelper;

        String managerAddress = clientConfig.getManagerAddress().get(0);
        String host = managerAddress.split(":")[0];
        int port = Integer.parseInt(managerAddress.split(":")[1]);

        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();

        //managedChannel.isTerminated()

        manageServiceClient = ManageServiceGrpc.newBlockingStub(managedChannel);
        this.buildNotifyUnitConnection(managedChannel);
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

    private void buildNotifyUnitConnection(ManagedChannel managedChannel) {
        ManageServiceGrpc.ManageServiceStub client = ManageServiceGrpc.newStub(managedChannel);

        NotifyUnitMessage msg = NotifyUnitMessage
                .newBuilder()
                .build();

        StreamObserver<NotifyUnitMessage> responseObserver = new StreamObserver<NotifyUnitMessage>() {
            @Override
            public void onNext(NotifyUnitMessage responseMessage) {
                log.debug("get notify unit");

                TransactionCleanService clean = beanHelper.loadTransactionCleanService(TransactionModel.LCN);
                clean.clear(responseMessage.getGroupId(),responseMessage.getState().getNumber());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                log.debug("notify unit completed");
            }
        };

        client.notifyUnit(msg,responseObserver);
    }
}