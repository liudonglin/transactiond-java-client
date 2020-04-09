package com.liudonglin.transactiond.tr.samples.order;

import com.liudonglin.transactiond.tr.core.config.EnableDistributedTransaction;
import com.liudonglin.transactiond.tr.core.tracing.http.FeignTracingTransmitter;
import com.liudonglin.transactiond.tr.samples.order.remote.FeignAccountService;
import feign.Feign;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@MapperScan("com.liudonglin.transactiond.tr.samples.order.dao")
@SpringBootApplication
@EnableDistributedTransaction
public class OrderStartup {
    public static void main(String[] args) {
        SpringApplication.run(OrderStartup.class, args);
    }

    @Bean
    public FeignAccountService getFeignAccountService() {
        return Feign.builder()
                .requestInterceptor(new FeignTracingTransmitter())
                .target(FeignAccountService.class, "http://localhost:6002");
    }
}
