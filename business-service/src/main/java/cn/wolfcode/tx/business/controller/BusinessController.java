package cn.wolfcode.tx.business.controller;


import cn.wolfcode.tx.business.TestDatas;
import cn.wolfcode.tx.business.service.IBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("businesses")
public class BusinessController {
    @Autowired
    private IBusinessService businessService;

    @GetMapping(value = "/purchase")
    public String purchase(Boolean rollback, Integer count) {
        int orderCount = 10;
        if (count != null) {
            orderCount = count;
        }
        try {
            businessService.purchase(TestDatas.USER_ID, TestDatas.COMMODITY_CODE, orderCount,
                    rollback == null ? false : rollback.booleanValue());
        } catch (Exception exx) {
            return "Purchase Failed:" + exx.getMessage();
        }
        return "SUCCESS";
    }
}
