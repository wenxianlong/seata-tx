package cn.wolfcode.tx.order.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_order_tx")
public class OrderTX {

    public static final int STATE_TRY = 0;
    public static final int STATE_CONFIRM = 1;
    public static final int STATE_CANCEL = 2;

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String txId;
    private int state = STATE_TRY;
}
