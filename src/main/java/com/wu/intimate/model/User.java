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
@TableName("tb_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userToken;//用户唯一标识（微信）
    private String headImg;//头像地址
    private String nickname;//昵称
    private String age;//年龄
    private String gender;//性别
    private String email;//邮箱
    private String address;//住址
    private String userIntro;//简介
    private String phone;//电话号
    private String password;//密码
    private String wechat;//微信标识
    private Date date;//注册时间
    private String authority;//权限
}
