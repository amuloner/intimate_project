package com.wu.intimate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wu.intimate.model.Admin;
import com.wu.intimate.model.Comments;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentsMapper extends BaseMapper<Comments> {
}
