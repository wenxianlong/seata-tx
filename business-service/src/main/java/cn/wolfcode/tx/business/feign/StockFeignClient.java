package cn.wolfcode.tx.business.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "stock-service")
public interface StockFeignClient {

    @GetMapping("/stocks/deduct")
    String deduct(@RequestParam("commodityCode") String commodityCode, @RequestParam("count") int count);

}
