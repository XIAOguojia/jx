package com.jx.grouppojo;

import com.jx.pojo.TbOrder;
import com.jx.pojo.TbOrderItem;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2019/4/26
 * Time:15:29
 */

public class MyOrder implements Serializable {

    //订单
    private List<TbOrder> orderList;
    //订单明细
    private Map<String,List<TbOrderItem>> orderItemMap;


    public List<TbOrder> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<TbOrder> orderList) {
        this.orderList = orderList;
    }

    public Map<String, List<TbOrderItem>> getOrderItemMap() {
        return orderItemMap;
    }

    public void setOrderItemMap(Map<String, List<TbOrderItem>> orderItemMap) {
        this.orderItemMap = orderItemMap;
    }
}
