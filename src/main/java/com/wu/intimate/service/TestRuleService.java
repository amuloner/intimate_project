package com.wu.intimate.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.intimate.model.TestRule;

import java.util.Map;

public interface TestRuleService extends IService<TestRule> {
    IPage<Map<String, String>> getTestRuleList(Page<Map<String, String>> pageParam, Map<String, String> param);

}
