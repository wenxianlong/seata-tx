package cn.wolfcode.tx.stock.service;

import cn.wolfcode.tx.stock.domain.Stock;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IStockService  extends IService<Stock> {

    /**
     * 扣库存
     * @param commodityCode
     * @param count
     */
    void deduct(String commodityCode, int count);
}
