package com.wu.intimate.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.intimate.model.Admin;
import com.wu.intimate.model.Comments;
import java.util.List;
import java.util.Map;

public interface CommentsService extends IService<Comments> {

    /**
     * 根据条件查询所有评论信息
     * @param pageParam
     * @param param
     * @return
     */
    IPage<Map<String, Object>> getCommentsByOpr(Page<Map<String, Object>> pageParam, Map<String,String> param);

    /**
     * 根据文章id数组删除相关评论
     * @param ids essayIds
     * @return
     */
    Boolean deleteCommentsByIds(List<Integer> ids);
}
