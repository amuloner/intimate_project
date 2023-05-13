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
@TableName("tb_test_q")
//测评-->问题
public class TestQues {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer qnum;
    private String title;
    private Integer testId;
    private Integer qtype;
    private Time ctime;
    private Integer sort;
    private Integer isdel;
}
