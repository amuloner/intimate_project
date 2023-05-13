package com.wu.intimate.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

/**
 * 回答表
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_answer")
public class Answer {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer questionId;//文章id
    private String content;//回答内容
    private Integer fromId;//回答者id
    private Integer toId;//被回复的回答者
    private Date date;//回答时间
    private Integer answerId;//次回答所属主回答id
    private Integer status;
}
