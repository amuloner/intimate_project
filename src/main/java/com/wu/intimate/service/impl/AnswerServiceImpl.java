package com.wu.intimate.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import com.wu.intimate.mapper.AnswerMapper;
import com.wu.intimate.mapper.QuestionMapper;
import com.wu.intimate.mapper.UserMapper;
import com.wu.intimate.model.*;
import com.wu.intimate.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements AnswerService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public IPage<Map<String, Object>> getAnswerByOpr(Page<Map<String, Object>> pageParam, Map<String, String> param) {
        String status = param.get("status");
        String fromName = param.get("fromName");
        String qTitle = param.get("qTitle");
        String questionId = param.get("questionId");
        QueryWrapper<Answer> queryWrapper = new QueryWrapper<>();


        if(!StringUtils.isNullOrEmpty(status))
            queryWrapper.eq("status",status);
        if(!StringUtils.isNullOrEmpty(questionId))
            queryWrapper.eq("questionId",questionId);
        if(!StringUtils.isNullOrEmpty(fromName)){
            List<User> userList = userMapper.selectList(new QueryWrapper<User>().like("nickname", fromName));
            //根据id条件拼接查询
            for (int i = 0; i < userList.size(); i++){
                //条件拼接
                int id = userList.get(i).getId();
                if(i == 0){
                    queryWrapper.eq("fromId",id);
                }else{
                    queryWrapper.or(qw1 -> qw1.eq("fromId",id));
                }
            }
        }
        if(!StringUtils.isNullOrEmpty(qTitle)){
            List<Question> qList = questionMapper.selectList(new QueryWrapper<Question>().like("title", qTitle));
            //根据id条件拼接查询
            for (int i = 0; i < qList.size(); i++){
                //条件拼接
                int id = qList.get(i).getId();
                if(i == 0){
                    queryWrapper.eq("questionId",id);
                }else{
                    queryWrapper.or(qw1 -> qw1.eq("questionId",id));
                }
            }
        }

        Page<Map<String, Object>> page = baseMapper.selectMapsPage(pageParam,queryWrapper);
        return page;
    }
}
