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
 * 文章
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_essay")
public class Essay {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String author;//作者
    private Integer authorId;//作者
    private String source;//来源
    private String title;//文章标题
    private String content;//内容
    private Date uploadTime;//上传时间
    private String label;//标签
    private String smallImg;//小图片
    private Integer readNum;//阅读量
    private Integer isDel;
    private Integer status;//审核状态
}
