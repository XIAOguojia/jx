package com.jx.grouppojo;

import com.jx.pojo.TbGoods;
import com.jx.pojo.TbGoodsDesc;
import com.jx.pojo.TbItem;

import java.io.Serializable;
import java.util.List;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2019/1/30
 * Time:16:17
 * 商品的组合实体类
 */
public class Goods implements Serializable {
    //商品SPU
    private TbGoods goods;
    //商品扩展信息
    private TbGoodsDesc goodsDesc;
    //商品SKU列表
    private List<TbItem> itemList;

    public TbGoods getGoods() {
        return goods;
    }

    public void setGoods(TbGoods goods) {
        this.goods = goods;
    }

    public TbGoodsDesc getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(TbGoodsDesc goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public List<TbItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<TbItem> itemList) {
        this.itemList = itemList;
    }
}
