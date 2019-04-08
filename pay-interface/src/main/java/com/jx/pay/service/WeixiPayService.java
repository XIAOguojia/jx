package com.jx.pay.service;

import java.util.Map;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2019/4/5
 * Time:13:58
 * 微信支付接口
 */
public interface WeixiPayService {
    /**
     * 生成微信支付二维码
     * @param out_trade_no 商户订单号
     * @param total_fee 金额(分)
     * @return
     */
    public Map createNative(String out_trade_no, String total_fee);

    /**
     * 查询支付状态
     * @param out_trade_no 商户订单号
     */
    public Map queryPayStatus(String out_trade_no);

}
