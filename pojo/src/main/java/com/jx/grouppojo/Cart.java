package com.jx.grouppojo;

import com.jx.pojo.TbOrderItem;

import java.io.Serializable;
import java.util.List;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2019/4/1
 * Time:10:27
 * 对每个商家的购物车进行的封装
 */
public class Cart implements Serializable {
    //商家ID
    private String sellerId;

    //商家名称
    private String sellerName;

    //购物车明细
    private List<TbOrderItem> orderItemList;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<TbOrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<TbOrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
