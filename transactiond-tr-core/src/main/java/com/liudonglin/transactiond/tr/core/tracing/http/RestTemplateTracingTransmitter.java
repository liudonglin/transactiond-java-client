package com.liudonglin.transactiond.tr.core.tracing.http;

import com.liudonglin.transactiond.tr.core.tracing.Tracings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@ConditionalOnClass(RestTemplate.class)
@Component
@Order
public class RestTemplateTracingTransmitter implements ClientHttpRequestInterceptor {

    @Autowired
    public RestTemplateTracingTransmitter(@Autowired(required = false) List<RestTemplate> restTemplates) {
        if (Objects.nonNull(restTemplates)) {
            restTemplates.forEach(restTemplate -> {
                List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
                interceptors.add(interceptors.size(), RestTemplateTracingTransmitter.this);
            });
        }
    }

    @Override
    @NonNull
    public ClientHttpResponse intercept(
            @NonNull HttpRequest httpRequest, @NonNull byte[] bytes,
            @NonNull ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        Tracings.transmit(httpRequest.getHeaders()::add);
        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }
}
