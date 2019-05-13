package com.jx.sellergoods.service;

import com.jx.pojo.TbItem;
import entity.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface ItemPageService {

    /**
     * 获取商品的详细信息（给骚年用）
     * @param goodsId 商品id
     * @return
     */
    Map getGoodsById(Long goodsId);

    /**
     * 添加购物车
     * @param cartList 购物车
     * @param goodsId 商品Id
     * @param num 数量
     * @return
     */
    List<Map> addToCart(List<Map> cartList,Long goodsId,Integer num);
}
