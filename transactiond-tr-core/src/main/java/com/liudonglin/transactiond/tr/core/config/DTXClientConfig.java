package com.liudonglin.transactiond.tr.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "transactiond.client")
@Component
public class DTXClientConfig {

    public DTXClientConfig() {
        this.dtxAspectOrder = 0;
        this.dtxTime = 30 * 1000;
        this.managerAddress = Collections.singletonList("127.0.0.1:6102");
    }

    /**
     * aspect order
     */
    private Integer dtxAspectOrder;

    /**
     * aspect connection order
     */
    private int resourceOrder;

    /**
     * 分布式事务持续最大时间
     */
    private long dtxTime;

    /**
     * manager hosts
     */
    private List<String> managerAddress;
}
