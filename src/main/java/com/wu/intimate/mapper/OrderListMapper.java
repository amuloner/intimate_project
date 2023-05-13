package com.wu.intimate.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wu.intimate.model.OrderList;
import com.wu.intimate.model.Star;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderListMapper extends BaseMapper<OrderList> {
    /**
     * 根据用户id查询预约表及相关咨询师信息
     * @param wrapper
     * @return
     */
    List<Map<String,String>> findByUserId(@Param(Constants.WRAPPER) QueryWrapper<Object> wrapper);

    /**
     * 根据咨询师id查询预约表及相关用户信息
     * @param wrapper
     * @return
     */
    List<Map<String,String>> findByConId(@Param(Constants.WRAPPER) QueryWrapper<Object> wrapper);
}
