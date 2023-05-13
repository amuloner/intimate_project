package com.wu.intimate.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_orderList")
public class OrderList {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer conId;
    private String username;
    private String time;
    private Integer orderIndex;
    private Integer status;
}
