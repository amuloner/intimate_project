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
 * 评论表
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_comments")
public class Comments {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer essayId;//文章id
    private String content;//评论内容
    private Integer fromId;//评论者id
    private Integer toId;//被回复的评论者
    private Integer status;//审核状态
    private Date date;//评论时间
    private Integer replyId;//次评论所属主评论id
}
