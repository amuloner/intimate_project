package com.wu.intimate.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wu.intimate.model.Consultant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ConsultantMapper extends BaseMapper<Consultant> {
    /**
     * 分页获取所有的咨询师数据 (用户数据、咨询师数据)
     * @param page
     * @param wrapper
     * @return
     */
    Page<Map<String,String>> findByPage(Page<Map<String, String>> page, @Param(Constants.WRAPPER) QueryWrapper<Map<String, String>> wrapper);

    /**
     * 根据id获取咨询师所有数据
     * @param wrapper
     * @return
     */
    Map<String,String> findById(@Param(Constants.WRAPPER) QueryWrapper<Object> wrapper);

    @Select("select id from tb_consultant")
    List<Integer> findAllId();
}
