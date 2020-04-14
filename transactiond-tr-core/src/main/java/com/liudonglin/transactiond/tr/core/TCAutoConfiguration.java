package com.liudonglin.transactiond.tr.core;

import com.liudonglin.transactiond.tr.core.config.DTXClientConfig;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class TCAutoConfiguration {

    private final DTXClientConfig clientConfig;

    @Autowired
    public TCAutoConfiguration(DTXClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }


    @Bean
    public ManagedChannel getManagedChannel(){
        String managerAddress = this.clientConfig.getManagerAddress().get(0);
        String host = managerAddress.split(":")[0];
        int port = Integer.parseInt(managerAddress.split(":")[1]);

        return ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
    }
}
