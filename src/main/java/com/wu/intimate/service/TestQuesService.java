package com.wu.intimate.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.intimate.model.TestQues;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface TestQuesService extends IService<TestQues> {

    IPage<Map<String, Object>> getTestQuesAndAnsByPage(Page<Map<String, Object>> pageParam, Map<String, String> param);
}
