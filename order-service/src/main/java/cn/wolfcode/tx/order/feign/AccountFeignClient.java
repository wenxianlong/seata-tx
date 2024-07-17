package cn.wolfcode.tx.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "account-service")
public interface AccountFeignClient {
    @GetMapping("/accounts/reduce")
    String reduce(@RequestParam("userId") String userId, @RequestParam("money") int money);


}
