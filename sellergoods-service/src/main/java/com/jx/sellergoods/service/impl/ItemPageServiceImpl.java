package com.jx.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.jx.mapper.TbGoodsDescMapper;
import com.jx.mapper.TbGoodsMapper;
import com.jx.mapper.TbItemMapper;
import com.jx.pojo.TbGoods;
import com.jx.pojo.TbGoodsDesc;
import com.jx.sellergoods.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import utils.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2019/5/6
 * Time:12:24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ItemPageServiceImpl implements ItemPageService {
    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public Map getGoodsById(Long goodsId) {

        //准备数据
        Map dataModel = new HashMap();
        //商品表数据
        TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
        String goodsName = goods.getGoodsName();
        BigDecimal price = goods.getPrice();
        dataModel.put("goodsName", goodsName);
        dataModel.put("price", price);
        //商品扩展表数据
        TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
        String spec = tbGoodsDesc.getSpecificationItems();

        String introduction = tbGoodsDesc.getIntroduction();
        String packageList = tbGoodsDesc.getPackageList();
        String images = tbGoodsDesc.getItemImages();

        dataModel.put("spec", JSON.parse(spec));
        String des = "{\"商品描述\":" + "\"" + introduction + "\"" + "," + "\"规格与包装\":" + "\"" + packageList + "\"" + "}";

        dataModel.put("des", JSON.parse(des));
        dataModel.put("images", JSON.parse(images));

//        TbItemExample example = new TbItemExample();
//        TbItemExample.Criteria criteria = example.createCriteria();
//        //goodsId来查找对应的SKU
//        criteria.andGoodsIdEqualTo(goodsId);
//        //必须为通过审核的商品
//        criteria.andStatusEqualTo("1");
//        //按照状态降序，保证第一个为默认
//        example.setOrderByClause("is_default desc");
//        List<TbItem> itemList = itemMapper.selectByExample(example);
//
//        dataModel.put("itemList", itemList);
        return dataModel;
    }

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Override
    public List<Map> addToCart(List<Map> cartList,Long goodsId, Integer num) {


        //商品表数据
        TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
        if (goods == null) {
            throw new RuntimeException("商品不存在");
        }
        Map dataModel = GoodsExist(goods, cartList);
        //判断商品是否已经存在
        if (dataModel.size() != 0) {
            //已存在
            num += (Integer) dataModel.get("num");
            dataModel.put("num", num);
            dataModel.put("price", goods.getPrice().doubleValue() * num);
        } else {//不存在
            dataModel.put("goodsName", goods.getGoodsName());
            dataModel.put("price", goods.getPrice().doubleValue() * num);
            dataModel.put("num", num);
            //商品扩展表数据
            TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
            String images = tbGoodsDesc.getItemImages();
            dataModel.put("images", JSON.parse(images));
            cartList.add(dataModel);
        }


        return cartList;
    }

    private Map GoodsExist(TbGoods goods, List<Map> cartList) {
        for (Map map : cartList) {
            if (map.get("goodsName").equals(goods.getGoodsName())) {
                return map;
            }
        }
        return new HashMap();
    }
}
