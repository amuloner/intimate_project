package com.wu.intimate.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Time;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_test_a")
//测评-->选项
public class TestAns {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String qId;
    private String direction;
    private Integer score;
    private Integer sort;
}
