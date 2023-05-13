package com.wu.intimate.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wu.intimate.model.TestList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface TestListMapper extends BaseMapper<TestList> {
    /**
     * 获取类别列表
     * @return
     */
    @Select("SELECT DISTINCT `label` FROM `tb_test`")
    List<String> findLabel();
}
