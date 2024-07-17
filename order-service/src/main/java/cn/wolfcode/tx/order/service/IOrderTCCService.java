package cn.wolfcode.tx.order.service;

import cn.wolfcode.tx.order.domain.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * TCC 二阶段提交业务接口
 */
@LocalTCC
public interface IOrderTCCService {
    /**
     * try-预扣款
     */
    @TwoPhaseBusinessAction(name="tryCreate", commitMethod = "confirm", rollbackMethod = "cancel")
    void tryCreate(@BusinessActionContextParameter(paramName = "userId") String userId,
                   @BusinessActionContextParameter(paramName = "commodityCode") String commodityCode,
                   @BusinessActionContextParameter(paramName = "orderCount") int orderCount);

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
