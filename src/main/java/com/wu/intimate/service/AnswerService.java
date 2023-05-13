package com.wu.intimate.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.intimate.model.Answer;
import com.wu.intimate.model.Evaluate;

import java.util.Map;


public interface AnswerService extends IService<Answer> {
    IPage<Map<String, Object>> getAnswerByOpr(Page<Map<String, Object>> pageParam, Map<String, String> param);
}
