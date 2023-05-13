package com.wu.intimate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import com.wu.intimate.mapper.TestResultMapper;
import com.wu.intimate.model.TestResult;
import com.wu.intimate.model.TestResult;
import com.wu.intimate.service.TestResultService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TestResultServiceImpl extends ServiceImpl<TestResultMapper, TestResult> implements TestResultService {

    @Override
    public IPage<Map<String, Object>> getTestResultByOpr(Page<Map<String, Object>> pageParam, Map<String, String> param) {
        QueryWrapper<TestResult> queryWrapper = new QueryWrapper<>();
        String userId = param.get("userId");


        if(!StringUtils.isNullOrEmpty(userId))
            queryWrapper.eq("userId", userId);

        return baseMapper.selectMapsPage(pageParam,queryWrapper);
    }
}
