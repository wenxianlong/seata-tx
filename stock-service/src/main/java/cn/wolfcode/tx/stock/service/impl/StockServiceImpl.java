package cn.wolfcode.tx.stock.service.impl;

import cn.wolfcode.tx.stock.domain.Stock;
import cn.wolfcode.tx.stock.mapper.StockMapper;
import cn.wolfcode.tx.stock.service.IStockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.core.context.RootContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements IStockService {
    @Override
    @Transactional
    public void deduct(String commodityCode, int count) {
        Stock one = lambdaQuery().eq(Stock::getCommodityCode, commodityCode).one();
        if(one != null && one.getCount() < count){
            throw new RuntimeException("Not Enough Count ...");
        }

        lambdaUpdate().setSql("count = count-" + count)
                .eq(Stock::getCommodityCode, commodityCode)
                .update();

    }
}
