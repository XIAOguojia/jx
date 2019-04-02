package com.jx.cart.service;

import com.jx.grouppojo.Cart;


import java.util.List;

/**
 * Created by intellij IDEA
 * Author:Raven
 * Date:2019/4/1
 * Time:10:32
 */
public interface CartService {
    /**
     * 将商品添加到购物车中
     * @param cartList 原来的购物车列表
     * @param itemId SKUID
     * @param num 数量
     * @return
     */
    public List<Cart> addGoodsToCart(List<Cart> cartList, Long itemId,Integer num);

    /**
     * 从redis中查询购物车
     * @param username 用户名
     * @return
     */
    public List<Cart> findCartListFromRedis(String username);

    /**
     * 将购物车保存到redis
     * @param username 用户名
     * @param cartList 购物车列表
     */
    public void saveCartListToRedis(String username,List<Cart> cartList);


    /**
     * 合并购物车
     * @param cartList1 购物车列表1
     * @param cartList2 购物车列表2
     * @return
     */
    public List<Cart> mergeCartList(List<Cart> cartList1,List<Cart> cartList2);

}
