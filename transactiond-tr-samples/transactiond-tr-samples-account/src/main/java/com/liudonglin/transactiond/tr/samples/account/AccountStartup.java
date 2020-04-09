package com.liudonglin.transactiond.tr.samples.account;

import com.liudonglin.transactiond.tr.core.config.EnableDistributedTransaction;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.liudonglin.transactiond.tr.samples.account.dao")
@SpringBootApplication
@EnableDistributedTransaction
public class AccountStartup {
    public static void main(String[] args) {
        SpringApplication.run(AccountStartup.class, args);
    }
}
