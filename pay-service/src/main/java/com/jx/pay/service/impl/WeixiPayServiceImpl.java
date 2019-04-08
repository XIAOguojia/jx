package com.jx.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.jx.pay.service.WeixiPayService;
import org.springframework.beans.factory.annotation.Value;
import utils.HttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2019/4/5
 * Time:14:00
 */
@Service
public class WeixiPayServiceImpl implements WeixiPayService {
    @Value("${appid}")
    private String appid;

    @Value("${partner}")
    private String partner;

    @Value("${partnerkey}")
    private String partnerkey;


    /**
     * 生成二维码
     *
     * @param out_trade_no 商户订单号
     * @param total_fee    金额(分)
     * @return
     */
    @Override
    public Map createNative(String out_trade_no, String total_fee) {
        //1、封装参数
        Map<String, String> map = new HashMap();
        //公众账号ID
        map.put("appid", appid);
        //商户号
        map.put("mch_id", partner);
        //随机字符串
        map.put("nonce_str", WXPayUtil.generateNonceStr());
        //商品描述
        map.put("body", "品优购");
        //商户订单号
        map.put("out_trade_no", out_trade_no);
        //标价金额（分）
        map.put("total_fee", total_fee);
        //终端IP
        map.put("spbill_create_ip", "127.0.0.1");
        //通知地址（随便写都行）
        map.put("notify_url", "http://hh.szu.cn");
        //交易类型
        map.put("trade_type", "NATIVE ");
        //2、生成要发送的xml
        try {
            String xmlParam = WXPayUtil.generateSignedXml(map, partnerkey);
//            xmlParam = new String(xmlParam.getBytes(), "ISO8859-1");
            System.out.println(xmlParam);
            //微信远程支付下单接口
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            client.setHttps(true);
            client.setXmlParam(xmlParam);
            client.post();

            //3、获得返回结果
            String result = client.getContent();
            System.out.println(result);

            Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
            Map<String, String> hashMap = new HashMap<>();
            //支付地址
            hashMap.put("code_url", resultMap.get("code_url"));
            //总金额
            hashMap.put("total_fee", total_fee);
            //订单号
            hashMap.put("out_trade_no", out_trade_no);
            return hashMap;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询支付状态
     *
     * @param out_trade_no 商户订单号
     * @return
     */
    @Override
    public Map queryPayStatus(String out_trade_no) {
        //1、封装参数
        Map<String, String> map = new HashMap();
        //公众账号ID
        map.put("appid", appid);
        //商户号
        map.put("mch_id", partner);
        //随机字符串
        map.put("nonce_str", WXPayUtil.generateNonceStr());
        //商户订单号
        map.put("out_trade_no", out_trade_no);

        //2、生成要发送的xml
        try {
            String xmlParam = WXPayUtil.generateSignedXml(map, partnerkey);
//            xmlParam = new String(xmlParam.getBytes(), "ISO8859-1");
            System.out.println(xmlParam);
            //微信远程支付查询接口
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setHttps(true);
            client.setXmlParam(xmlParam);
            client.post();

            //3、获得返回结果
            String result = client.getContent();

            Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
            System.out.println(resultMap);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}


