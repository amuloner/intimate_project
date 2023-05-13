package com.wu.intimate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import com.wu.intimate.mapper.TestListMapper;
import com.wu.intimate.model.Essay;
import com.wu.intimate.model.TestList;
import com.wu.intimate.service.TestListService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TestListServiceImpl extends ServiceImpl<TestListMapper, TestList> implements TestListService {
    /**
     * pageIndex、pageSize、label、orderType、status、title
     * @param pageParam
     * @param param
     * @return
     */
    @Override
    public IPage<TestList> getTestTitleByOpr(Page<TestList> pageParam, Map<String, String> param) {
        QueryWrapper<TestList> queryWrapper = new QueryWrapper<>();
        String label = param.get("label");
        String orderType = param.get("orderType");
        String status = param.get("status");
        String title = param.get("title");

        if(!StringUtils.isNullOrEmpty(label) && !label.equals("全部"))
            queryWrapper.like("label",label);
        if(!StringUtils.isNullOrEmpty(orderType)){
            if(orderType.equals("date")){
                queryWrapper.orderByDesc("ctime");
            }else if (orderType.equals("hot")){
                queryWrapper.orderByDesc("id");
            }
        }

        if(!StringUtils.isNullOrEmpty(title))
            queryWrapper.like("title", title);
        if(!StringUtils.isNullOrEmpty(status))
            queryWrapper.eq("status", status);

        return baseMapper.selectPage(pageParam,queryWrapper);
    }

    @Override
    public List<String> getTestLabels() {
        return baseMapper.findLabel();
    }
}
