package com.jx.sellergoods.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jx.mapper.*;
import com.jx.pojo.*;
import com.jx.grouppojo.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jx.sellergoods.service.GoodsService;

import entity.PageResult;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbGoods> findAll() {
        return goodsMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbBrandMapper brandMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbSellerMapper sellerMapper;

    /**
     * 增加
     */
    @Override
    public void add(Goods goods) {
        //状态：未审核
        goods.getGoods().setAuditStatus("0");
        //插入商品数据
        goodsMapper.insert(goods.getGoods());
        System.out.println(goods.getGoods().getId());
        //设置ID
        goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());
        //插入商品扩展数据
        goodsDescMapper.insert(goods.getGoodsDesc());

        if ("1".equals(goods.getGoods().getIsEnableSpec())) {


            List<TbItem> itemList = goods.getItemList();
            for (TbItem tbItem : itemList) {
                //标题
                String title = goods.getGoods().getGoodsName();
                Map<String, Object> map = JSON.parseObject(tbItem.getSpec());
                for (String key : map.keySet()) {
                    title += " " + map.get(key);
                }
                tbItem.setTitle(title);
                setItemValus(goods, tbItem);
                itemMapper.insert(tbItem);
            }
        } else {
            TbItem tbitem = new TbItem();
            tbitem.setTitle(goods.getGoods().getGoodsName());//商品KPU+规格描述串作为SKU名称
            tbitem.setPrice(goods.getGoods().getPrice());//价格
            tbitem.setStatus("1");//状态
            tbitem.setIsDefault("1");//是否默认
            tbitem.setNum(99999);//库存数量
            tbitem.setSpec("{}");
            setItemValus(goods, tbitem);

            itemMapper.insert(tbitem);

        }
    }

    private void setItemValus(Goods goods, TbItem tbItem) {
        //商品SPU编号
        tbItem.setGoodsId(goods.getGoods().getId());
        //商家编号
        tbItem.setSellerId(goods.getGoods().getSellerId());
        //商品分类编号（3级）
        tbItem.setCategoryid(goods.getGoods().getCategory3Id());
        //创建日期
        tbItem.setCreateTime(new Date());
        //修改日期
        tbItem.setUpdateTime(new Date());
        //品牌名称
        TbBrand brand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
        tbItem.setBrand(brand.getName());
        //分类名称
        TbItemCat tbItemCat = itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
        tbItem.setCategory(tbItemCat.getName());
        //商家名称
        TbSeller seller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
        tbItem.setSeller(seller.getNickName());

        //图片地址（取spu的第一个图片）
        List<Map> imageList = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
        if (imageList.size() > 0) {
            tbItem.setImage((String) imageList.get(0).get("url"));
        }
    }


    /**
     * 修改
     */
    @Override
    public void update(TbGoods goods) {
        goodsMapper.updateByPrimaryKey(goods);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbGoods findOne(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            goodsMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbGoodsExample example = new TbGoodsExample();
        TbGoodsExample.Criteria criteria = example.createCriteria();

        if (goods != null) {
            if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
                criteria.andSellerIdLike("%" + goods.getSellerId() + "%");
            }
            if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
                criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
            }
            if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
                criteria.andAuditStatusLike("%" + goods.getAuditStatus() + "%");
            }
            if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
                criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
            }
            if (goods.getCaption() != null && goods.getCaption().length() > 0) {
                criteria.andCaptionLike("%" + goods.getCaption() + "%");
            }
            if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
                criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
            }
            if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
                criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
            }
            if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
                criteria.andIsDeleteLike("%" + goods.getIsDelete() + "%");
            }

        }

        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

}
