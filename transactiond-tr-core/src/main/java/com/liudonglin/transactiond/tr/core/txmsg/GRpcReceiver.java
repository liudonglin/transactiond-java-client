package com.liudonglin.transactiond.tr.core.txmsg;

import com.liudonglin.transactiond.tr.core.transaction.TransactionControlTemplate;
import com.liudonglin.transactiond.tr.core.transaction.TransactionState;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import txmsg.ManageServiceGrpc;
import txmsg.NotifyUnitMessage;

@Component
@Slf4j
public class GRpcReceiver {

    private TransactionControlTemplate transactionControlTemplate;

    private ManagedChannel managedChannel;

    @Autowired
    public GRpcReceiver(TransactionControlTemplate transactionControlTemplate,
                        ManagedChannel managedChannel) {
        this.transactionControlTemplate = transactionControlTemplate;
        this.managedChannel = managedChannel;
        this.buildNotifyUnitConnection(this.managedChannel);
    }

    private void buildNotifyUnitConnection(ManagedChannel managedChannel) {
        ManageServiceGrpc.ManageServiceStub client = ManageServiceGrpc.newStub(managedChannel);

        NotifyUnitMessage msg = NotifyUnitMessage
                .newBuilder()
                .build();

        StreamObserver<NotifyUnitMessage> responseObserver = new StreamObserver<NotifyUnitMessage>() {
            @SneakyThrows
            @Override
            public void onNext(NotifyUnitMessage responseMessage) {
                log.debug("get notify unit");

                transactionControlTemplate.clearGroup(responseMessage.getGroupId()
                        , TransactionState.valueOf(responseMessage.getState().getNumber()), responseMessage.getUnitId() );

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
