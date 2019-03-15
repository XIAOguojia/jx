package com.jx.search.service.impl;

import java.util.*;

import com.jx.pojo.TbItem;
import com.jx.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;
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

        //2、查询分类列表
        List<String> list = searchCategoryList(searchMap);
        map.put("categoryList",list);

        //3、查询品牌和规格列表
        if (!"".equals(searchMap.get("category"))){
            //有分类名称则按照分类名称来查
            map.putAll(searchBrandAndSpec((String) searchMap.get("category")));
        }else {
            if (list.size()>0){
                //选第一个名称作为查询的名称
                map.putAll(searchBrandAndSpec(list.get(0)));
            }
        }

        return map;
    }

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询品牌和规格列表
     * @param category 分类名称
     * @return
     */
    private Map searchBrandAndSpec(String category){
        Map map = new HashMap();
        //获取模板ID
        Long id = (Long) redisTemplate.boundHashOps("itemCat").get(category);
        if (id!=null){
            //根据模板ID查询品牌列表,并放入map中
            map.put("brandList",redisTemplate.boundHashOps("brandList").get(id));
            //根据模板ID查询规格列表,并放入map中
            map.put("specList",redisTemplate.boundHashOps("specList").get(id));
        }
        return map;
    }
    /**
     * 查询分类列表
     * @param searchMap
     * @return
     */
    private List<String> searchCategoryList(Map searchMap){
        List<String> list = new ArrayList<>();
        Query query = new SimpleQuery("*:*");
        //按照关键字查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        //设置分组选项
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(groupOptions);
        //得到分组页
        GroupPage<TbItem> groupPage = solrTemplate.queryForGroupPage(query, TbItem.class);
        //得到分组结果集
        GroupResult<TbItem> itemCategory = groupPage.getGroupResult("item_category");
        //得到分组结果入口页
        Page<GroupEntry<TbItem>> groupEntries = itemCategory.getGroupEntries();
        //得到分组入口集合
        List<GroupEntry<TbItem>> content = groupEntries.getContent();
        for (GroupEntry<TbItem> entry : content) {
            //将分组结果的名称封装到返回值中
            list.add(entry.getGroupValue());
        }
        return list;
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
        //按分类过滤查询
        if (!"".equals(searchMap.get("category"))){
            Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
            FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(filterQuery);
        }

        //按品牌过滤查询
        if (!"".equals(searchMap.get("brand"))){
            Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
            SimpleFilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(filterQuery);
        }

        //按规格过滤查询
        if(searchMap.get("spec")!=null){
            Map<String,String> specMap= (Map) searchMap.get("spec");
            for(String key:specMap.keySet() ){
                Criteria filterCriteria=new Criteria("item_spec_"+key).is( specMap.get(key) );
                FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }


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
