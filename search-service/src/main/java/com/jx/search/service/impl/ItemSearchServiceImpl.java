package com.jx.search.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jx.pojo.TbItem;
import com.jx.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;
import com.alibaba.dubbo.config.annotation.Service;


@Service(timeout = 5000)
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map search(Map searchMap) {
        Map map = new HashMap();
/*
        Query query = new SimpleQuery();
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);

        map.put("rows", page.getContent());*/

        //1、查询列表以及高亮显示
        map.putAll(searchByKeyWordsAndHighLight(searchMap));

        return map;
    }

    /**
     * 根据关键字进行搜索，并完成高亮显示
     *
     * @param searchMap
     * @return
     */
    private Map searchByKeyWordsAndHighLight(Map searchMap) {
        Map map = new HashMap();

        HighlightQuery query = new SimpleHighlightQuery();
        //设置高亮字段
        HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");
        //高亮前缀
        highlightOptions.setSimplePrefix("<em style='color:red'>");
        //高亮后缀
        highlightOptions.setSimplePostfix("</em>");
        //设置高亮选项
        query.setHighlightOptions(highlightOptions);
        //按照关键字查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        HighlightPage<TbItem> highlightPage = solrTemplate.queryForHighlightPage(query, TbItem.class);
        //高亮入口的集合
        List<HighlightEntry<TbItem>> highlightEntryList = highlightPage.getHighlighted();
        for (HighlightEntry<TbItem> entry : highlightEntryList) {
            //获取实体
            TbItem item = entry.getEntity();
            //判断是否为空
            if (entry.getHighlights().size()>0 && entry.getHighlights().get(0).getSnipplets().size()>0){
                //设置名称高亮
                item.setTitle(entry.getHighlights().get(0).getSnipplets().get(0));
            }
        }

        map.put("rows",highlightPage.getContent());
        return map;
    }
}
