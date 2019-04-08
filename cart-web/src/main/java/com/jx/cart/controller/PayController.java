package com.jx.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jx.order.service.OrderService;
import com.jx.pay.service.WeixiPayService;
import com.jx.pojo.TbPayLog;
import entity.Result;
import org.omg.CORBA.TIMEOUT;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.IdWorker;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2019/4/5
 * Time:14:16
 * 支付控制层
 */
@RestController
@RequestMapping("/pay")
public class PayController {
    @Reference(timeout = 5000)
    private WeixiPayService weixiPayService;

    @Reference
    private OrderService orderService;

    @RequestMapping("/createNative")
    public Map<String, String> createNative() {
        //获取当前用户
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        //获取支付日志
        TbPayLog payLog = orderService.searchPayLogFromRedis(userId);
        if (payLog != null) {
            return weixiPayService.createNative(payLog.getOutTradeNo(), payLog.getTotalFee() + "");
        } else {
            return new HashMap<>();
        }

    }

    /**
     * 查询支付状态
     *
     * @param out_trade_no 商户订单号
     * @return
     */
    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no) {
        Result result = null;
        int x = 0;
        while (true) {
            Map map = weixiPayService.queryPayStatus(out_trade_no);
            if (map == null) {
                result = new Result(false, "没有该订单");
                break;
            }
            if ("SUCCESS".equals(map.get("trade_state"))) {
                result = new Result(true, "支付成功");
                //修改订单状态
                orderService.updateOrderStatus(out_trade_no, (String) map.get("transaction_id"));

                break;
            }
            //每三秒查询一次订单状态
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (++x > 100) {
                result = new Result(false, "二维码超时");
                break;
            }
        }

        return result;
    }


}
