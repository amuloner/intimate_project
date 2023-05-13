package com.wu.intimate.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wu.intimate.model.Admin;
import com.wu.intimate.model.Essay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EssayMapper extends BaseMapper<Essay> {
    /**
     * 根据用户id查询发表文章所获总赞数
     * @param queryWrapper
     * @return
     */
    @Select("SELECT SUM(starNum) FROM tb_essay where ${ew.sqlSegment}")
    Integer selectEssayNum(@Param(Constants.WRAPPER) QueryWrapper<Essay> queryWrapper);
}
