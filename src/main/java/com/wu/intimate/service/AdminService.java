package com.wu.intimate.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.intimate.model.Admin;
import com.wu.intimate.model.LoginForm;

public interface AdminService extends IService<Admin> {
    Admin login(LoginForm loginForm);
}
