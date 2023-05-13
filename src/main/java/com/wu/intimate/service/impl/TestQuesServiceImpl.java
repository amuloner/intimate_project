package com.wu.intimate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import com.wu.intimate.mapper.TestQuesMapper;
import com.wu.intimate.model.TestQues;
import com.wu.intimate.service.TestQuesService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TestQuesServiceImpl extends ServiceImpl<TestQuesMapper, TestQues> implements TestQuesService {
    @Override
    public IPage<Map<String, Object>> getTestQuesAndAnsByPage(Page<Map<String, Object>> pageParam, Map<String, String> param) {
        QueryWrapper<TestQues> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isNullOrEmpty(param.get("testId")))
            queryWrapper.eq("testId",param.get("testId"));

        return baseMapper.selectMapsPage(pageParam, queryWrapper);
    }
}
