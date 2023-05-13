package com.wu.intimate.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_ques_star")
public class QStar {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer qId;//问题id
    private Integer userId;//用户id
    private Integer isHug;//是否抱抱
    private Integer isSame;//是否同感
}
