package cn.wolfcode.tx.order.service.impl;

import cn.wolfcode.tx.order.domain.Order;
import cn.wolfcode.tx.order.domain.OrderTX;
import cn.wolfcode.tx.order.feign.AccountFeignClient;
import cn.wolfcode.tx.order.mapper.OrderMapper;
import cn.wolfcode.tx.order.mapper.OrderTXMapper;
import cn.wolfcode.tx.order.service.IOrderService;
import cn.wolfcode.tx.order.service.IOrderTCCService;
import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderTCCServiceImpl  implements IOrderTCCService {

    @Autowired
    private AccountFeignClient accountFeignClient;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderTXMapper orderTXMapper;

    @Override
    public void tryCreate(String userId, String commodityCode, int count) {
        System.err.println("---------tryCreate-----------");

        //业务悬挂
        OrderTX orderTX = orderTXMapper.selectOne(new LambdaQueryWrapper<OrderTX>().eq(OrderTX::getTxId, RootContext.getXID()));
        if (orderTX != null){
            //存在，说明已经canel执行过类，拒绝服务
            return;
        }

        // 定单总价 = 订购数量(count) * 商品单价(100)
        int orderMoney = count * 100;
        // 生成订单
        Order order = new Order();
        order.setCount(count);
        order.setCommodityCode(commodityCode);
        order.setUserId(userId);
        order.setMoney(orderMoney);
        orderMapper.insert(order);

        OrderTX tx = new OrderTX();
        tx.setTxId(RootContext.getXID());
        tx.setState(OrderTX.STATE_TRY);
        orderTXMapper.insert(tx);

        // 调用账户余额扣减
        String result = accountFeignClient.reduce(userId, orderMoney);
        if (!"SUCCESS".equals(result)) {
            throw new RuntimeException("Failed to call Account Service. ");
        }
    }

    @Override
    public boolean confirm(BusinessActionContext ctx) {
        System.err.println("---------confirm-----------");

        //删除记录
        int ret = orderTXMapper.delete(new LambdaQueryWrapper<OrderTX>().eq(OrderTX::getTxId, ctx.getXid()));
        return ret == 1;
    }

    @Override
    public boolean cancel(BusinessActionContext ctx) {
        System.err.println("---------cancel-----------" );
        String userId = ctx.getActionContext("userId").toString();
        String commodityCode = ctx.getActionContext("commodityCode").toString();
        OrderTX orderTX = orderTXMapper.selectOne(new LambdaQueryWrapper<OrderTX>().eq(OrderTX::getTxId, ctx.getXid()));
        if (orderTX == null){
            //为空， 空回滚
            orderTX = new OrderTX();
            orderTX.setTxId(ctx.getXid());
            orderTX.setState(OrderTX.STATE_CANCEL);
            orderTXMapper.insert(orderTX);
            return true;
        }
        //幂等处理
        if(orderTX.getState() == OrderTX.STATE_CANCEL){
            return true;
        }

        //恢复余额
        orderMapper.delete(new LambdaQueryWrapper<Order>().eq(Order::getUserId, userId).eq(Order::getCommodityCode, commodityCode));

        orderTX.setState(OrderTX.STATE_CANCEL);
        int ret = orderTXMapper.updateById(orderTX);
        return ret == 1;
    }
}
