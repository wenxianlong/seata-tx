package cn.wolfcode.tx.account.service;


import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * TCC 二阶段提交业务接口
 */
@LocalTCC
public interface IAccountTCCService {
    /**
     * try-预扣款
     */
    @TwoPhaseBusinessAction(name="tryReduce", commitMethod = "confirm", rollbackMethod = "cancel")
    void tryReduce(@BusinessActionContextParameter(paramName = "userId") String userId,
                   @BusinessActionContextParameter(paramName = "money") int money);
    /**
     * confirm-提交
     * @param ctx
     * @return
     */
    boolean confirm(BusinessActionContext ctx);
    /**
     * cancel-回滚
     * @param ctx
     * @return
     */
    boolean cancel(BusinessActionContext ctx);
}
