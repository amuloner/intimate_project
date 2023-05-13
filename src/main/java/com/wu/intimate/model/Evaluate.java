package com.wu.intimate.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

//咨询评价类
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_evaluate")
public class Evaluate {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer fromId;//评价用户id
    private Integer toId;//被评价咨询师
    private String content;//评价内容
    private Date date;//评价时间
    private String isAnonymous;//是否匿名
}
