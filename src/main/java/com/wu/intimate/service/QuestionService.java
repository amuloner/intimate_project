package com.wu.intimate.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.intimate.model.Evaluate;
import com.wu.intimate.model.Question;

import java.util.Map;


public interface QuestionService extends IService<Question> {
    /**
     * 根据条件分页查询所有提问信息
     * @param pageParam
     * @param param
     * @return
     */
    IPage<Map<String,Object>> getQuestionByOpr(Page<Map<String,Object>> pageParam, Map<String,String> param);
}
