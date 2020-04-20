package com.liudonglin.transactiond.tr.core.log;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Objects;

@Data
@Component
@ConfigurationProperties(value = "dtx.log")
public class H2DbProperties {

    private String filePath;

    public H2DbProperties(
            @Autowired(required = false) ConfigurableEnvironment environment,
            @Autowired(required = false) ServerProperties serverProperties) {
        String applicationName = "application";
        Integer port = 0;
        if (Objects.nonNull(environment)) {
            applicationName = environment.getProperty("spring.application.name");
        }
        if (Objects.nonNull(serverProperties)) {
            port = serverProperties.getPort();
        }
        this.filePath = System.getProperty("user.dir") +
                File.separator +
                ".txlcn" +
                File.separator +
                (StringUtils.hasText(applicationName) ? applicationName : "application") +
                "-" + port;
    }
}
