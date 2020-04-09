package com.liudonglin.transactiond.tr.samples.account.service;

import com.liudonglin.transactiond.tr.samples.account.dao.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class AccountService {

    @Autowired
    private AccountMapper accountDao;

    /**
     * 扣减账户余额
     * @param userId 用户id
     * @param money 金额
     */
    public void decrease(Long userId, BigDecimal money) throws Exception {

        log.info("------->扣减账户开始account中");
        //模拟超时异常，全局事务回滚
//        try {
//            Thread.sleep(30*1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        int updateCount = accountDao.decrease(userId,money);
        if (updateCount ==0){
            throw new Exception("余额不足，下单失败");
        }
        log.info("------->扣减账户结束account中");

        //修改订单状态，此调用会导致调用成环
        /*LOGGER.info("修改订单状态开始");
        String mes = orderApi.update(userId, money.multiply(new BigDecimal("0.09")),0);
        LOGGER.info("修改订单状态结束：{}",mes);*/
    }

}
