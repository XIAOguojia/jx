package com.jx.manager.controller;

import java.util.Arrays;
import java.util.List;

import com.jx.grouppojo.Goods;
import com.jx.page.service.ItemPageService;
import com.jx.pojo.TbItem;
import com.jx.search.service.ItemSearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.jx.pojo.TbGoods;
import com.jx.sellergoods.service.GoodsService;

import entity.PageResult;
import entity.Result;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbGoods> findAll() {
        return goodsService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return goodsService.findPage(page, rows);
    }


    /**
     * 修改
     *
     * @param goods
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Goods goods) {
        try {
            goodsService.update(goods);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public Goods findOne(Long id) {
        return goodsService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            goodsService.delete(ids);

            itemSearchService.deleteByGoodsIds(Arrays.asList(ids));

            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param goods
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbGoods goods, int page, int rows) {
        return goodsService.findPage(goods, page, rows);
    }

    @Reference
    private ItemSearchService itemSearchService;

    @Reference(timeout = 5000)
    private ItemPageService itemPageService;

    /**
     * 批量修改状态
     *
     * @param ids
     * @param status
     * @return
     */
    @RequestMapping("/updateStatus")
    public Result updateStatus(Long[] ids, String status) {
        try {
            goodsService.updateStatus(ids, status);
            //审核通过，更新索引库
            if ("1".equals(status)) {
                List<TbItem> itemListByGoodsIdandStatus = goodsService.findItemListByGoodsIdandStatus(ids, status);
                System.out.println(itemListByGoodsIdandStatus.size());
                if (itemListByGoodsIdandStatus.size() > 0) {
                    itemSearchService.importItemList(itemListByGoodsIdandStatus);
                } else {
                    System.out.println("没有明细数据");
                }

            }

            return new Result(true, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "失败");
        }
    }

    /**
     * 测试生成静态页面
     *
     * @param goodsId
     */
    @RequestMapping("/genItemHtml")
    public void genItemHtml(Long goodsId) {
        itemPageService.genItemHtml(goodsId);
    }
}
