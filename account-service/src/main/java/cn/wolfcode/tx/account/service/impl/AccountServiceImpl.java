package cn.wolfcode.tx.account.service.impl;

import cn.wolfcode.tx.account.domain.Account;
import cn.wolfcode.tx.account.mapper.AccountMapper;
import cn.wolfcode.tx.account.service.IAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.core.context.RootContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reduce(String userId, int money) {
        Account one = lambdaQuery().eq(Account::getUserId, userId).one();
        if(one != null && one.getMoney() < money){
            throw new RuntimeException("Not Enough Money ...");
        }
        lambdaUpdate().setSql("money = money - " + money)
                        .eq(Account::getUserId, userId)
                        .update();

    }
}
