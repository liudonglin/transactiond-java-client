package com.liudonglin.transactiond.tr.samples.order;

import com.liudonglin.transactiond.tr.core.config.EnableDistributedTransaction;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.liudonglin.transactiond.tr.samples.order.dao")
@SpringBootApplication
@EnableDistributedTransaction
public class OrderStartup {
    public static void main(String[] args) {
        SpringApplication.run(OrderStartup.class, args);
    }
}
