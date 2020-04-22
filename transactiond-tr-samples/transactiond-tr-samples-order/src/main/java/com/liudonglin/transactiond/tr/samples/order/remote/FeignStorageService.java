package com.liudonglin.transactiond.tr.samples.order.remote;

import feign.Param;
import feign.RequestLine;

public interface FeignStorageService {

    @RequestLine("GET /storage/decrease?productId={productId}&count={count}")
    String decrease(@Param("productId") Long productId, @Param("count") Integer count);

}
