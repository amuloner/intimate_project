package com.wu.intimate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import com.wu.intimate.mapper.UserMapper;
import com.wu.intimate.model.Consultant;
import com.wu.intimate.model.User;
import com.wu.intimate.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public IPage<User> getUserByOpr(Page<User> pageParam, Map<String, String> param) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isNullOrEmpty(param.get("nickname")))
            queryWrapper.like("nickname",param.get("nickname"));
        if(!StringUtils.isNullOrEmpty(param.get("authority")))
            queryWrapper.eq("authority",param.get("authority"));

        Page<User> page = baseMapper.selectPage(pageParam,queryWrapper);
        return page;
    }

    @Override
    public User getUserById(Integer userId) {
        return baseMapper.selectById(userId);
    }

    @Override
    public Map<String, String> getUser(QueryWrapper<Object> wrapper) {
        return baseMapper.findUser(wrapper);
    }


}
