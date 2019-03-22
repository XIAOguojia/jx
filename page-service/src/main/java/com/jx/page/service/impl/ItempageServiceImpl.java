package com.jx.page.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.jx.mapper.TbGoodsDescMapper;
import com.jx.mapper.TbGoodsMapper;
import com.jx.mapper.TbItemCatMapper;
import com.jx.mapper.TbItemMapper;
import com.jx.page.service.ItemPageService;
import com.jx.pojo.TbGoods;
import com.jx.pojo.TbGoodsDesc;
import com.jx.pojo.TbItem;
import com.jx.pojo.TbItemExample;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2019/3/20
 * Time:16:52
 */

@Service(timeout = 5000)
public class ItempageServiceImpl implements ItemPageService {
    @Value("${pagedir}")
    private String pagedir;

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public boolean genItemHtml(Long goodsId) {
        try {
            //创建配置类
            Configuration configuration = freeMarkerConfig.getConfiguration();
            //加载模板
            Template template = configuration.getTemplate("item.ftl");

            //准备数据
            Map dataModel = new HashMap();
            //商品表数据
            TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
            dataModel.put("goods",goods);
            //商品扩展表数据
            TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
            dataModel.put("goodsDesc",tbGoodsDesc);

            String category1Name = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
            String category2Name = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();

            String category3Name = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
            dataModel.put("category1Name",category1Name);
            dataModel.put("category2Name",category2Name);
            dataModel.put("category3Name",category3Name);

            TbItemExample example = new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            //goodsId来查找对应的SKU
            criteria.andGoodsIdEqualTo(goodsId);
            //必须为通过审核的商品
            criteria.andStatusEqualTo("1");
            //按照状态降序，保证第一个为默认
            example.setOrderByClause("is_default desc");
            List<TbItem> itemList = itemMapper.selectByExample(example);
            for (TbItem item : itemList) {
                System.out.println(item.getTitle()+"  "+item.getSpec());
            }
            dataModel.put("itemList",itemList);

            //创建writer对象
            Writer out =  new FileWriter(pagedir+goodsId+".html");
            //输出
            template.process(dataModel,out);

            //关闭流
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
