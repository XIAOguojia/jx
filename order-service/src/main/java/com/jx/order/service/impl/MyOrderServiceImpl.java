package com.jx.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.jx.grouppojo.MyOrder;
import com.jx.mapper.TbOrderItemMapper;
import com.jx.mapper.TbOrderMapper;
import com.jx.mapper.TbPayLogMapper;
import com.jx.order.service.MyOrderService;

import com.jx.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2019/4/26
 * Time:15:40
 */
@Service
public class MyOrderServiceImpl implements MyOrderService {
    @Autowired
    private TbOrderMapper orderMapper;

    @Autowired
    private TbOrderItemMapper orderItemMapper;

    /**
     * 查找我的订单
     * @param userId 用户名
     * @return
     */
    @Override
    public MyOrder findAllByUserId(String userId) {
        MyOrder myOrder = new MyOrder();
        TbOrderExample example = new TbOrderExample();
        TbOrderExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        //获取订单
        List<TbOrder> orderList = orderMapper.selectByExample(example);
        myOrder.setOrderList(orderList);

        for (TbOrder tbOrder : orderList) {
            System.out.print(tbOrder.getOrderId()+"  ");
        }
        System.out.println();

        Map<String,List<TbOrderItem>> orderItemMap = new HashMap<>();
        for (TbOrder tbOrder : orderList) {
            //获取订单Id
            Long orderId = tbOrder.getOrderId();
            //根据订单Id获取对应的明细订单
            TbOrderItemExample exam = new TbOrderItemExample();
            TbOrderItemExample.Criteria orderItemCriteria = exam.createCriteria();
            orderItemCriteria.andOrderIdEqualTo(orderId);
            List<TbOrderItem> orderItemList = orderItemMapper.selectByExample(exam);
            //放入明细map中
            orderItemMap.put(String.valueOf(orderId),orderItemList);
        }

        myOrder.setOrderItemMap(orderItemMap);
        return myOrder;
    }

    @Override
    public void confirmOrder(Long orderId) {
        TbOrder tbOrder = orderMapper.selectByPrimaryKey(orderId);
        tbOrder.setStatus("3");
        orderMapper.updateByPrimaryKey(tbOrder);

    }

    @Autowired
    private TbPayLogMapper payLogMapper;

    @Override
    public TbPayLog findByOrderId(String orderId) {
        TbPayLogExample example = new TbPayLogExample();
        TbPayLogExample.Criteria criteria = example.createCriteria();
        criteria.andOrderListEqualTo(orderId);

        List<TbPayLog> tbPayLogs =  payLogMapper.selectByExample(example);
        return tbPayLogs.get(0);
    }

}
