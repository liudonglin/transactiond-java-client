package com.liudonglin.transactiond.tr.samples.storage.service;

import com.liudonglin.transactiond.tr.samples.storage.dao.StorageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StorageService {

    @Autowired
    private StorageMapper storageDao;

    /**
     * 扣减库存
     * @param productId 产品id
     * @param count 数量
     * @return
     */
    public void decrease(Long productId, Integer count) {
        log.info("------->扣减库存开始");
        storageDao.decrease(productId,count);
        log.info("------->扣减库存结束");
    }
}
