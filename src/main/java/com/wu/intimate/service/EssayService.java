package com.wu.intimate.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.intimate.model.Admin;
import com.wu.intimate.model.Essay;
import com.wu.intimate.model.User;
import io.swagger.models.auth.In;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface EssayService extends IService<Essay> {

    /**
     * 根据用户id获取文章点赞总数
     * @param userId
     * @return
     */
    Integer getEssayNumByUserId(Integer userId);

    /**
     * 根据用户ids获取文章
     * @param ids 用户idList
     * @return
     */
    List<Essay> getAllEssayByIds(List<Integer> ids);

    /**
     * 根据条件查询所有文章信息
     * @param pageParam
     * @param param
     * @return
     */
    IPage<Map<String,Object>> getEssayByOpr(Page<Map<String,Object>> pageParam, Map<String,String> param);
}
