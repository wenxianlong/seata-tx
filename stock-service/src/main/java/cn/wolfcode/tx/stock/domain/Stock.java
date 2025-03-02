package cn.wolfcode.tx.stock.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_stock")
public class Stock {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String commodityCode;
    private Integer count;
}
