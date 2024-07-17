package cn.wolfcode.tx.order.controller;

import cn.wolfcode.tx.order.domain.Order;
import cn.wolfcode.tx.order.service.IOrderService;
import cn.wolfcode.tx.order.service.IOrderTCCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("orders")
public class OrderController {

    @Autowired
    private IOrderTCCService orderTCCService;

    @GetMapping(value = "/create")
    public String create(String userId, String commodityCode, int orderCount) {
        try {
            orderTCCService.tryCreate(userId, commodityCode, orderCount);
        } catch (Exception exx) {
            exx.printStackTrace();
            return "FAIL";
        }
        return "SUCCESS";
    }
}
