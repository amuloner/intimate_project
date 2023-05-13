package com.wu.intimate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wu.intimate.model.Chat;
import com.wu.intimate.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper extends BaseMapper<Chat> {

}
