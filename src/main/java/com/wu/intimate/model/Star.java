package com.wu.intimate.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

//点赞映射类
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_essaystar")
public class Star {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer essayId;//文章id
    private Integer userId;//用户id
}
