package com.liudonglin.transactiond.tr.samples.account.dao;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface AccountMapper {

    /**
     * 扣减账户余额
     * @param userId 用户id
     * @param money 金额
     */
    int decrease(@Param("userId") Long userId, @Param("money") BigDecimal money);

    /**
     * 增加账户余额
     * @param userId
     * @param money
     * @return
     */
    int increase(@Param("userId") Long userId, @Param("money") BigDecimal money);

    /**
     * 查询用户余额
     * @param userId
     * @return
     */
    BigDecimal getMoneyByUserId(@Param("userId") Long userId);

}
