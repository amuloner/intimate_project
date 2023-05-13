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
@TableName("tb_question")
public class Question {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;//提问者
    private String title;//问题标题
    private String content;//内容
    private Date date;//提问时间
    private Integer hugNum;//抱抱数量
    private Integer sameNum;//同感数量
}
