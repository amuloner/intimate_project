package com.wu.intimate.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wu.intimate.model.TestRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface TestRuleMapper extends BaseMapper<TestRule> {
    Page<Map<String,String>> findByPage(Page<Map<String, String>> page, @Param(Constants.WRAPPER) QueryWrapper<Map<String, String>> wrapper);
}
