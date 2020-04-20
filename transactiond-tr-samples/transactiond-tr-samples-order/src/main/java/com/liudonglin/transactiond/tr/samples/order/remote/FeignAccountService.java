package com.liudonglin.transactiond.tr.samples.order.remote;

import feign.Param;
import feign.RequestLine;

import java.math.BigDecimal;

public interface FeignAccountService {

    @RequestLine("GET /account/decrease?userId={userId}&money={money}")
    String decrease(@Param("userId") Long userId, @Param("money") BigDecimal money);

    @RequestLine("GET /account/decreasetcc?userId={userId}&money={money}")
    String decreaseTcc(@Param("userId") Long userId, @Param("money") BigDecimal money);

}
