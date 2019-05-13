package com.jx.order.service;


import com.jx.grouppojo.MyOrder;
import com.jx.pojo.TbPayLog;

import java.math.BigInteger;

/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface MyOrderService {
    /**
     * 根据用户名查找对应的所有订单
     * @param userId 用户名
     * @return
     */
	MyOrder findAllByUserId(String userId);

    /**
     * 确认收货
     * @param orderId  订单的Id
     */
	void confirmOrder(Long orderId);

    /**
     * 根据订单Id去查找未付款的订单
     * @param orderId 订单Id
     * @return
     */
	TbPayLog findByOrderId(String orderId);
}
