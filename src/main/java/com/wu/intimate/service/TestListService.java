package com.wu.intimate.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.intimate.model.TestList;

import java.util.List;
import java.util.Map;

public interface TestListService extends IService<TestList> {

    IPage<TestList> getTestTitleByOpr(Page<TestList> pageParam, Map<String, String> param);

    List<String> getTestLabels();
}
