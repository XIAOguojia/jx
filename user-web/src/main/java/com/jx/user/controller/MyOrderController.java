package com.jx.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jx.grouppojo.MyOrder;
import com.jx.order.service.MyOrderService;
import com.jx.order.service.OrderService;
import com.jx.pay.service.WeixiPayService;
import com.jx.pojo.TbPayLog;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2019/4/26
 * Time:15:52
 */
@RestController
@RequestMapping("/order")
public class MyOrderController {
    @Reference
    private MyOrderService myOrderService;

    @Reference(timeout = 5000)
    private WeixiPayService weixiPayService;

    @Reference
    private OrderService orderService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAllByUserId")
    public MyOrder findAllByUserId() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return myOrderService.findAllByUserId(userId);
    }

    /**
     * 确认收货
     *
     * @return
     */
    @RequestMapping("/confirmOrder")
    public Result confirmOrder(Long orderId) {
        try {
            System.out.println(orderId);
            myOrderService.confirmOrder(orderId);
            return new Result(true,"确认收货成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"确认收货失败");
        }
    }



    @RequestMapping("/createNative")
    public Map<String, String> createNative(String orderId) {
        //获取当前用户
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(orderId);
        //获取支付日志
        TbPayLog payLog = myOrderService.findByOrderId(orderId);

        if (payLog != null) {
            return weixiPayService.createNative(payLog.getOutTradeNo(), payLog.getTotalFee() + "");
        } else {
            return new HashMap<>();
        }

    }
}
