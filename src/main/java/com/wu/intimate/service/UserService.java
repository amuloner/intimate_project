package com.wu.intimate.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.intimate.model.Consultant;
import com.wu.intimate.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {
    /**
     * 根据条件查询所有用户信息
     * @param pageParam
     * @param param
     * @return
     */
    IPage<User> getUserByOpr(Page<User> pageParam, Map<String,String> param);

    /**
     * 根据用户id获取用户信息
     * @param userId
     * @return
     */
    User getUserById(Integer userId);

    Map<String,String> getUser(QueryWrapper<Object> wrapper);

}
