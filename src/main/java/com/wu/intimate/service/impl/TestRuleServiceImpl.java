package com.wu.intimate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import com.wu.intimate.mapper.TestAnsMapper;
import com.wu.intimate.mapper.TestRuleMapper;
import com.wu.intimate.model.TestAns;
import com.wu.intimate.model.TestRule;
import com.wu.intimate.service.TestAnsService;
import com.wu.intimate.service.TestRuleService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TestRuleServiceImpl extends ServiceImpl<TestRuleMapper, TestRule> implements TestRuleService {

    @Override
    public IPage<Map<String, String>> getTestRuleList(Page<Map<String, String>> pageParam, Map<String, String> param) {
        QueryWrapper<Map<String, String>> wrapper = new QueryWrapper<>();
        if(!StringUtils.isNullOrEmpty(param.get("username")))
            wrapper.like("tru.username", param.get("username"));
        return baseMapper.findByPage(pageParam, wrapper);
    }
}
