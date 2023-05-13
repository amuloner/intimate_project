package com.wu.intimate.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.sql.Time;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_test")
//测评
public class TestList {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String sub_title;
    private String sub_cn;
    private String sub_en;
    private String direction;
    private Integer isFree;
    private Integer money;
    private String thkMsg;
    private Date ctime;
    private Integer testminute;
    private Date startTime;
    private Date endTime;
    private String editor;
    private String label;
    private String image;
    private String status;
}
