package cn.wolfcode.tx.account.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_account")
public class Account {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userId;
    private int money;
}
