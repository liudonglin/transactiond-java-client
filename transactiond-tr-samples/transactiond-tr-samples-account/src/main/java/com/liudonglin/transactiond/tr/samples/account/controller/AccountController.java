package com.liudonglin.transactiond.tr.samples.account.controller;

import com.liudonglin.transactiond.tr.samples.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("account")
public class AccountController {

    @Autowired
    private AccountService accountServiceImpl;

    /**
     * 扣减账户余额
     * @param userId 用户id
     * @param money 金额
     * @return
     */
    @RequestMapping(value = "/decrease", method = RequestMethod.GET)
    public @ResponseBody
    String decrease(@RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money) throws Exception {
        accountServiceImpl.decrease(userId,money);
        return "Account decrease success";
    }
}
