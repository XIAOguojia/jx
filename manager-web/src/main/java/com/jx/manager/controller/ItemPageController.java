package com.jx.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;

import com.jx.sellergoods.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.CookieUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2019/5/6
 * Time:11:43
 */
@RestController
@RequestMapping("/item")
public class ItemPageController {
    @Reference
    private ItemPageService itemPageService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @RequestMapping("/info.do")
    public Map getGoodById(Long goodsId) {
        System.out.println(goodsId);
        return itemPageService.getGoodsById(goodsId);
    }

    @RequestMapping("/addCart.do")
    public List<Map> addToCart(Long goodsId, Integer num) {
        System.out.println(goodsId + "  " + num);
        //1、获取购物车列表
        String cartListStr = CookieUtil.getCookieValue(request, "cart", "UTF-8");
        if (cartListStr == null || cartListStr.trim().equals("")) {
            cartListStr = "[]";
        }
        List<Map> cartList = JSON.parseArray(cartListStr, Map.class);
        //2、向购物车添加商品
        cartList =  itemPageService.addToCart(cartList,goodsId, num);
        //3、将购物车存入cookie
        CookieUtil.setCookie(request, response, "cart", JSON.toJSONString(cartList), 3600 * 24, "UTF-8");

        return cartList;
    }

    @RequestMapping("/findCart.do")
    public List<Map> findCart() {
        //1、从cookie中取出购物车,字符串格式
        String cartListStr = CookieUtil.getCookieValue(request, "cart", "UTF-8");
        return JSON.parseArray(cartListStr, Map.class);
    }

}
