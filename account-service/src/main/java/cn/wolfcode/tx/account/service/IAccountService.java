package cn.wolfcode.tx.account.service;

import cn.wolfcode.tx.account.domain.Account;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IAccountService  extends IService<Account> {

    /**
     * 账户扣款
     * @param userId
     * @param money
     * @return
     */
    void reduce(String userId, int money);
}
