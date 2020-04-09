package com.liudonglin.transactiond.tr.core.tracing.http;

import com.liudonglin.transactiond.tr.core.tracing.Tracings;
import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@ConditionalOnClass(Feign.class)
@Component
@Order
public class FeignTracingTransmitter implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Tracings.transmit(requestTemplate::header);
    }
}