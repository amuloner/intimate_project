package com.wu.intimate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.intimate.mapper.OrderListMapper;
import com.wu.intimate.model.OrderList;
import com.wu.intimate.service.OrderListService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderListServiceImpl extends ServiceImpl<OrderListMapper, OrderList> implements OrderListService {
    @Override
    public List<Map<String, String>> getOrderListByUserId(Integer userId, Integer type) {
        return baseMapper.findByUserId(new QueryWrapper<>().apply("u.id = o.conId and o.userId = {0} and o.status = {1}", userId,type));
    }

    @Override
    public List<Map<String, String>> getOrderListByConId(Integer conId, Integer type) {
        return baseMapper.findByConId(new QueryWrapper<>().apply("u.id = o.userId and o.conId = {0} and o.status = {1}", conId,type));
    }
}
