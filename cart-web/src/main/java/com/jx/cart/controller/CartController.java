package com.jx.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;

import com.jx.cart.service.CartService;
import com.jx.grouppojo.Cart;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2019/4/1
 * Time:11:25
 */
@RestController
@RequestMapping("/cart")
public class CartController {
    @Reference(timeout = 6000)
    private CartService cartService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @RequestMapping("/addGoodsToCartList")
    public Result addGoodsToCartList(Long itemId, Integer num) {
        //得到登陆人账号,判断当前是否有人登陆
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            //1、获取购物车列表
            List<Cart> cartList = findCartList();
            //2、向购物车添加商品
            cartList = cartService.addGoodsToCart(cartList, itemId, num);
            if (username.equals("anonymousUser")) {
                //3、将购物车存入cookie
                CookieUtil.setCookie(request, response, "cartList", JSON.toJSONString(cartList), 3600 * 24, "UTF-8");
            } else {
                //将购物车存入redis
                cartService.saveCartListToRedis(username, cartList);
            }
            return new Result(true, "添加购物车成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加购物车失败");
        }
    }

    /**
     * 获取购物车列表
     *
     * @return
     */
    @RequestMapping("/findCartList")
    public List<Cart> findCartList() {
        //得到登陆人账号,判断当前是否有人登陆
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //1、从cookie中取出购物车,字符串格式
        String cartListStr = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
        if (cartListStr == null || cartListStr.trim() == "") {
            cartListStr = "[]";
        }
        List<Cart> cartList_cookie = JSON.parseArray(cartListStr, Cart.class);
        //如果用户未登录
        if (username.equals("anonymousUser")) {
            return cartList_cookie;
        } else {
            //用户已登录
            List<Cart> cartList_redis = cartService.findCartListFromRedis(username);
            //如果有本地购物车就合并
            if (cartList_cookie.size() > 0) {
                cartList_redis = cartService.mergeCartList(cartList_cookie, cartList_redis);
                //清空本地购物车
                CookieUtil.deleteCookie(request, response, "cartList");
                //存入redis
                cartService.saveCartListToRedis(username, cartList_redis);
            }
            return cartList_redis;
        }
    }


}
