package com.wu.intimate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wu.intimate.model.Admin;
import com.wu.intimate.model.Star;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StarMapper extends BaseMapper<Star> {
}
