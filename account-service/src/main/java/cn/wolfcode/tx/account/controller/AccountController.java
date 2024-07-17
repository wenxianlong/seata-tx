package cn.wolfcode.tx.account.controller;

import cn.wolfcode.tx.account.service.IAccountTCCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("accounts")
public class AccountController {

    //@Autowired
    //private IAccountService accountService;
    @Autowired
    private IAccountTCCService accountTCCService;

    @GetMapping(value = "/reduce")
    public String reduce(String userId, int money) {
        try {
            accountTCCService.tryReduce(userId, money);
        } catch (Exception exx) {
            exx.printStackTrace();
            return "FAIL";
        }
        return "SUCCESS";
    }

}
