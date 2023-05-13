package com.wu.intimate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.intimate.model.OrderList;

import java.util.List;
import java.util.Map;

public interface OrderListService extends IService<OrderList> {

    /**
     * 根据用户id查询预约表及咨询师数据
     * @param userId
     * @return
     */
    List<Map<String, String>> getOrderListByUserId(Integer userId, Integer type);

    /**
     * 根据咨询师id查询预约表及用户据
     * @param conId
     * @return
     */
    List<Map<String, String>> getOrderListByConId(Integer conId, Integer type);
}
