package cn.wolfcode.tx.order.service;

import cn.wolfcode.tx.order.domain.Order;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IOrderService  extends IService<Order> {

    /**
     * 创建订单
     */
    void create(String userId, String commodityCode, int orderCount);
}
