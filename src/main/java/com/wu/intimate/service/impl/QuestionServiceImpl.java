package com.wu.intimate.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import com.wu.intimate.mapper.QuestionMapper;
import com.wu.intimate.mapper.UserMapper;
import com.wu.intimate.model.Question;

import com.wu.intimate.model.User;
import com.wu.intimate.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public IPage<Map<String,Object>> getQuestionByOpr(Page<Map<String,Object>> pageParam, Map<String, String> param) {
        String username = param.get("username");
        String title = param.get("title");

        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isNullOrEmpty(username)){
            List<User> userList = userMapper.selectList(new QueryWrapper<User>().like("nickname", username));
            //根据id条件拼接查询
            for (int i = 0; i < userList.size(); i++){
                //条件拼接
                int id = userList.get(i).getId();
                if(i == 0){
                    queryWrapper.eq("userId",id);
                }else{
                    queryWrapper.or(qw1 -> qw1.eq("userId",id));
                }
            }
        }

        if(!StringUtils.isNullOrEmpty(title)){
            queryWrapper.like("title", title);
            queryWrapper.or(qw1 -> qw1.like("content", title));
        }

        Page<Map<String,Object>> page = baseMapper.selectMapsPage(pageParam,queryWrapper);
        return page;
    }
}
