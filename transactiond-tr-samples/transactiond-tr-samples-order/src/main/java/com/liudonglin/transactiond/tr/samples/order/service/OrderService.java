package com.liudonglin.transactiond.tr.samples.order.service;

import com.liudonglin.transactiond.tr.core.annotation.LcnTransaction;
import com.liudonglin.transactiond.tr.samples.order.dao.OrderMapper;
import com.liudonglin.transactiond.tr.samples.order.entity.Order;
import com.liudonglin.transactiond.tr.samples.order.remote.FeignAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderDao;

    @Autowired
    private FeignAccountService feignAccountService;

    @LcnTransaction
    @Transactional
    public void create(Order order) {
        log.info("------->交易开始");
        //本地方法
        orderDao.create(order);

        //远程方法 扣减库存
        //storageApi.decrease(order.getProductId(),order.getCount());

        //远程方法 扣减账户余额
        log.info("------->扣减账户开始order中");
        feignAccountService.decreaseTcc(order.getUserId(),order.getMoney());
        log.info("------->扣减账户结束order中");

        log.info("------->交易结束");
    }

}
