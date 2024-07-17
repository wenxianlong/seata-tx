package cn.wolfcode.tx.stock.service;

import cn.wolfcode.tx.stock.domain.Stock;
import com.baomidou.mybatisplus.extension.service.IService;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * TCC 二阶段提交业务接口
 */
@LocalTCC
public interface IStockTCCService  {
    /**
     * try-预扣款
     */
    @TwoPhaseBusinessAction(name="tryDeduct", commitMethod = "confirm", rollbackMethod = "cancel")
    void tryDeduct(@BusinessActionContextParameter(paramName = "commodityCode") String commodityCode,
                   @BusinessActionContextParameter(paramName = "count") int count);

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
