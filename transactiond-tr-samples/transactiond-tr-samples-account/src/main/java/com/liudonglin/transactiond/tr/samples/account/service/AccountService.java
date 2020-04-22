package com.liudonglin.transactiond.tr.samples.account.service;

import com.liudonglin.transactiond.tr.core.annotation.LcnTransaction;
import com.liudonglin.transactiond.tr.core.annotation.TccTransaction;
import com.liudonglin.transactiond.tr.samples.account.dao.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @LcnTransaction
    @Transactional
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

    /**
     *
     * @param userId
     * @param money
     * @throws Exception
     */
    @TccTransaction(confirmMethod = "decreaseConfirm",cancelMethod = "decreaseCancel")
    public void decreaseTcc(Long userId, BigDecimal money) throws Exception {

        /**
         * 该处tcc模式实现得并不正确，仅测试验证回滚功能
         */

        log.info("------->检查账户余额");
        BigDecimal residue = accountDao.getMoneyByUserId(userId);
        if(residue.compareTo(money)<0){
            throw new Exception("余额不足，下单失败");
        }
        accountDao.decrease(userId,money);
        log.info("------->锁定账户余额");
    }

    public void decreaseConfirm(Long userId, BigDecimal money) {
        log.info("------->扣减账户开始account中");
        log.info("------->扣减账户结束account中");
    }

    public void decreaseCancel(Long userId, BigDecimal money) {
        log.info("------->添加账户开始account中");
        accountDao.increase(userId,money);
        log.info("------->添加账户开始account中");
    }

}
