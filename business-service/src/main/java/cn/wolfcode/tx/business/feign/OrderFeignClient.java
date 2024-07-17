package cn.wolfcode.tx.business.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service")
public interface OrderFeignClient {

    @GetMapping("/orders/create")
    String create(@RequestParam("userId") String userId, @RequestParam("commodityCode") String commodityCode,
                  @RequestParam("orderCount") int orderCount);

}
