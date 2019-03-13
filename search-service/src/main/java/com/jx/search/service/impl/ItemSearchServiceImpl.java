package com.jx.search.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.jx.pojo.TbItem;
import com.jx.search.service.ItemSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@Service(timeout=5000)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-solr.xml")
public class ItemSearchServiceImpl implements ItemSearchService {

	@Autowired
	private SolrTemplate solrTemplate;
	
	@Override
	public Map search(Map searchMap) {
		Map map=new HashMap();

		Query query=new SimpleQuery();
		Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
		
		map.put("rows", page.getContent());
		
		return map;
	}

	@Test
    public void man() {
        Map<String,Object> map=new HashMap<>();
        Query query=new SimpleQuery();
        //添加查询条件
        Map searchMap= new HashMap();
        searchMap.put("keywords","手机");
        Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
        map.put("rows", page.getContent());
        System.out.println(map.get("rows"));
    }
}
