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
}
