package com.wu.intimate.model;

import lombok.Data;

@Data
/**
 * 登录表单pojo类
 */
public class LoginForm {
    private String username;
    private String password;
    private String verifiCode;//验证码
    private Integer userType;
}
