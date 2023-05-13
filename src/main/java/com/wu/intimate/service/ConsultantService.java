package com.wu.intimate.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.intimate.model.Consultant;

import java.util.Map;

public interface ConsultantService extends IService<Consultant> {

    /**
     * 根据条件查询所有咨询师数据 (用户数据、咨询师数据)
     * @param pageParam
     * @param param
     * @return
     */
    IPage<Map<String, String>> getConsultantByOpr(Page<Map<String, String>> pageParam, Map<String, String> param);

    /**
     * 根据id获取咨询师所有信息
     * @param conId
     * @return
     */
    Map<String, String> getConsultantById(Integer conId);
}
