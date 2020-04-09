package com.liudonglin.transactiond.tr.samples.storage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.liudonglin.transactiond.tr.samples.storage.dao")
@SpringBootApplication
public class StorageStartup {
    public static void main(String[] args) {
        SpringApplication.run(StorageStartup.class, args);
    }
}
