package com.wu.intimate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.intimate.mapper.AdminMapper;
import com.wu.intimate.model.Admin;
import com.wu.intimate.model.LoginForm;
import com.wu.intimate.service.AdminService;
import com.wu.intimate.utils.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional//事务控制
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Override
    public Admin login(LoginForm loginForm) {
        /**
         * queryWrapper是mybatis plus中实现查询的对象封装操作类--条件构造器
         */
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq("username",loginForm.getUsername());
        wrapper.eq("password", MD5.encrypt(loginForm.getPassword()));

        Admin admin = baseMapper.selectOne(wrapper);
        return admin;
    }
}
