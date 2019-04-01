package com.jx.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.jx.cart.service.CartService;
import com.jx.grouppojo.Cart;
import com.jx.mapper.TbItemMapper;
import com.jx.pojo.TbItem;
import com.jx.pojo.TbOrderItem;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2019/4/1
 * Time:10:52
 */
@Service
public class CartServicerImpl implements CartService {
    @Autowired
    private TbItemMapper itemMapper;

    /**
     * 将商品添加进购物车
     *
     * @param cartList 原来的购物车列表
     * @param itemId   SKUID
     * @param num      数量
     * @return
     */
    @Override
    public List<Cart> addGoodsToCart(List<Cart> cartList, Long itemId, Integer num) {
        //1.根据商品SKU ID查询SKU商品信息
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        if (item == null) {
            throw new RuntimeException("商品不存在");
        }
        if (!item.getStatus().equals("1")) {
            throw new RuntimeException("商品状态无效");
        }

        //2.获取商家ID
        String sellerId = item.getSellerId();
        //3.根据商家ID判断购物车列表中是否存在该商家的购物车
        Cart cart = searchCartBySellerId(cartList, sellerId);

        //4.如果购物车列表中不存在该商家的购物车
        if (cart == null) {
            //4.1 新建购物车对象
            cart = new Cart();
            cart.setSellerId(sellerId);
            cart.setSellerName(item.getSeller());
            TbOrderItem orderItem = createTbOrderItem(item, num);
            List<TbOrderItem> orderItemList = new ArrayList<>();
            orderItemList.add(orderItem);
            cart.setOrderItemList(orderItemList);
            //4.2 将新建的购物车对象添加到购物车列表
            cartList.add(cart);
        } else {
            //5.如果购物车列表中存在该商家的购物车
            // 查询购物车明细列表中是否存在该商品
            TbOrderItem orderItem = searchOrderItemByItemId(cart.getOrderItemList(), itemId);
            //5.1. 如果没有，新增购物车明细
            if (orderItem == null) {
                orderItem = createTbOrderItem(item, num);
                cart.getOrderItemList().add(orderItem);
            } else {
                //5.2. 如果有，在原购物车明细上添加数量，更改金额
                orderItem.setNum(orderItem.getNum() + num);
                orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue() * orderItem.getNum()));
                //如果操作后数量小于0，则移出此条记录
                if (orderItem.getNum() <= 0) {
                    cart.getOrderItemList().remove(orderItem);
                }
                //如果移出此条记录后，购物车明细为空，则删除这个购物车
                if (cart.getOrderItemList().size() <= 0) {
                    cartList.remove(cart);
                }
            }
        }


        return cartList;
    }

    /**
     * 查询购物车明细列表中是否存在该商品
     *
     * @param orderItemList 购物车明细列表
     * @param itemId        sku id
     */
    private TbOrderItem searchOrderItemByItemId(List<TbOrderItem> orderItemList, Long itemId) {
        for (TbOrderItem orderItem : orderItemList) {
            if (orderItem.getItemId().longValue() == itemId.longValue()) {
                return orderItem;
            }
        }
        return null;
    }

    /**
     * 创建购物车明细
     *
     * @param item SKU
     * @param num  数量
     * @return
     */
    private TbOrderItem createTbOrderItem(TbItem item, Integer num) {
        if (num < 0) {
            throw new RuntimeException("数量非法");
        }

        TbOrderItem orderItem = new TbOrderItem();
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setNum(num);
        orderItem.setSellerId(item.getSellerId());
        orderItem.setItemId(item.getId());
        orderItem.setPicPath(item.getImage());
        orderItem.setPrice(item.getPrice());
        orderItem.setTitle(item.getTitle());
        orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue() * num));
        return orderItem;
    }

    /**
     * 根据商家Id来查找对应的购物车
     *
     * @param cartList
     * @param sellerId
     * @return
     */
    private Cart searchCartBySellerId(List<Cart> cartList, String sellerId) {
        for (Cart cart : cartList) {
            if (cart.getSellerId().equals(sellerId)) {
                return cart;
            }
        }

        return null;
    }
}

