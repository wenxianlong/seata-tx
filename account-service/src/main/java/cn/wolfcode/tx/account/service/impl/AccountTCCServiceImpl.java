package cn.wolfcode.tx.account.service.impl;

import cn.wolfcode.tx.account.domain.Account;
import cn.wolfcode.tx.account.domain.AccountTX;
import cn.wolfcode.tx.account.mapper.AccountMapper;
import cn.wolfcode.tx.account.mapper.AccountTXMapper;
import cn.wolfcode.tx.account.service.IAccountService;
import cn.wolfcode.tx.account.service.IAccountTCCService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;

@Service
public class AccountTCCServiceImpl  implements IAccountTCCService {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private AccountTXMapper accountTXMapper;
    @Override
    public void tryReduce(String userId, int money) {
        System.err.println("-----------tryReduce-------------" + RootContext.getXID());
        //业务悬挂
        AccountTX accountTX = accountTXMapper.selectOne(new LambdaQueryWrapper<AccountTX>().eq(AccountTX::getTxId, RootContext.getXID()));
        if (accountTX != null){
            //存在，说明已经canel执行过类，拒绝服务
            return;
        }
        Account one = accountMapper.selectOne(new LambdaQueryWrapper<Account>().eq(Account::getUserId, userId));
        if(one != null && one.getMoney() < money){
            throw new RuntimeException("Not Enough Money ...");
        }
        LambdaUpdateWrapper<Account> wrapper = new LambdaUpdateWrapper<>();
        wrapper.setSql("money = money - " + money);
        wrapper.eq(Account::getUserId, userId);

        accountMapper.update(null, wrapper);


        AccountTX tx = new AccountTX();
        tx.setFreezeMoney(money);
        tx.setTxId(RootContext.getXID());
        tx.setState(AccountTX.STATE_TRY);

        accountTXMapper.insert(tx);

    }

    @Override
    public boolean confirm(BusinessActionContext ctx) {
        System.err.println("-----------confirm-------------");
        //删除记录
        int ret = accountTXMapper.delete(new LambdaQueryWrapper<AccountTX>().eq(AccountTX::getTxId, ctx.getXid()));
        return ret == 1;

    }
    @Override
    public boolean cancel(BusinessActionContext ctx) {
        System.err.println("-----------cancel-------------");
        String userId = ctx.getActionContext("userId").toString();
        String money = ctx.getActionContext("money").toString();

        AccountTX accountTX = accountTXMapper.selectOne(new LambdaQueryWrapper<AccountTX>().eq(AccountTX::getTxId, ctx.getXid()));
        if (accountTX == null){
            //为空， 空回滚
            accountTX = new AccountTX();
            accountTX.setTxId(ctx.getXid());
            accountTX.setState(AccountTX.STATE_CANCEL);
            if(money != null){
                accountTX.setFreezeMoney(Integer.parseInt(money));
            }
            accountTXMapper.insert(accountTX);
            return true;
        }
        //幂等处理
        if(accountTX.getState() == AccountTX.STATE_CANCEL){
            return true;
        }

        //恢复余额
        accountMapper.update(null, new LambdaUpdateWrapper<Account>()
                        .setSql("money = money + " + money)
                .eq(Account::getUserId, userId));

        accountTX.setFreezeMoney(0);
        accountTX.setState(AccountTX.STATE_CANCEL);
        int ret = accountTXMapper.updateById(accountTX);
        return ret == 1;
    }
}
