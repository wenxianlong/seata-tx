package cn.wolfcode.tx.stock.service.impl;

import cn.wolfcode.tx.stock.domain.Stock;
import cn.wolfcode.tx.stock.domain.StockTX;
import cn.wolfcode.tx.stock.mapper.StockMapper;
import cn.wolfcode.tx.stock.mapper.StockTXMapper;
import cn.wolfcode.tx.stock.service.IStockService;
import cn.wolfcode.tx.stock.service.IStockTCCService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockTCCServiceImpl implements IStockTCCService {

    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private StockTXMapper stockTXMapper;

    @Override
    public void tryDeduct(String commodityCode, int count) {
        System.err.println("---------tryDeduct-----------");
        //业务悬挂
        StockTX stockTX = stockTXMapper.selectOne(new LambdaQueryWrapper<StockTX>().eq(StockTX::getTxId, RootContext.getXID()));
        if (stockTX != null){
            //存在，说明已经canel执行过类，拒绝服务
            return;
        }
        Stock one = stockMapper.selectOne(new LambdaQueryWrapper<Stock>().eq(Stock::getCommodityCode, commodityCode));
        if(one != null && one.getCount() < count){
            throw new RuntimeException("Not Enough Count ...");
        }
        stockMapper.update(null, new LambdaUpdateWrapper<Stock>()
                .setSql("count = count-" + count)
                .eq(Stock::getCommodityCode, commodityCode));

        StockTX tx = new StockTX();
        tx.setCount(count);
        tx.setTxId(RootContext.getXID());
        tx.setState(StockTX.STATE_TRY);

        stockTXMapper.insert(tx);

    }

    @Override
    public boolean confirm(BusinessActionContext ctx) {
        System.err.println("---------confirm-----------");
        //删除记录
        int ret = stockTXMapper.delete(new LambdaQueryWrapper<StockTX>().eq(StockTX::getTxId, ctx.getXid()));
        return ret == 1;

    }

    @Override
    public boolean cancel(BusinessActionContext ctx) {
        System.err.println("---------cancel-----------");
        String count = ctx.getActionContext("count").toString();
        String commodityCode = ctx.getActionContext("commodityCode").toString();
        StockTX stockTX = stockTXMapper.selectOne(new LambdaQueryWrapper<StockTX>().eq(StockTX::getTxId, ctx.getXid()));
        if (stockTX == null){
            //为空， 空回滚
            stockTX = new StockTX();
            stockTX.setTxId(ctx.getXid());
            stockTX.setState(StockTX.STATE_CANCEL);
            if(count != null){
                stockTX.setCount(Integer.parseInt(count));
            }
            stockTXMapper.insert(stockTX);
            return true;
        }
        //幂等处理
        if(stockTX.getState() == StockTX.STATE_CANCEL){
            return true;
        }
        //恢复余额
        stockMapper.update(null, new LambdaUpdateWrapper<Stock>()
                .setSql("count = count + " + count)
                .eq(Stock::getCommodityCode, commodityCode));

        stockTX.setCount(0);
        stockTX.setState(StockTX.STATE_CANCEL);
        int ret = stockTXMapper.updateById(stockTX);
        return ret == 1;
    }
}
