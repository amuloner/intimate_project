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
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
/*
  咨询师对象
 */
@TableName("tb_consultant")
public class Consultant {
    private Integer id;
    private String name;//姓名
    private String idCard;//身份证号
    private String achieve;//头衔、标签，中国心理学会会员、国家二级心理咨询师
    private String realm;//擅长领域
    private String conDate;//咨询时间 [00000,00000,00000,00000,00000,00000]
    private Integer conNum;//咨询次数
    private Integer conLikes;//咨询点赞量
    private Integer complaintsNum;//投诉次数
    private Date atcationDate;//认证日期
    private String conIntro;//简介
    private Integer status;//审核状态
    private String certifiedUrl;//认证资料url
    private Integer isDel;
}
