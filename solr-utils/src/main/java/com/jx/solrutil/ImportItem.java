package com.jx.solrutil;


import com.alibaba.fastjson.JSON;
import com.jx.mapper.TbItemMapper;
import com.jx.pojo.TbItem;
import com.jx.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2019/3/11
 * Time:15:14
 * 批量数据导入
 */
@Component("importItem")
public class ImportItem {
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private TbItemMapper itemMapper;

    private void importAllItem(){
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        //只查询状态为 1 的数据
        criteria.andStatusEqualTo("1");
        List<TbItem> list = itemMapper.selectByExample(example);
        for (TbItem item : list) {
            /*
            spec是POJO中另外的属性
            item.getSpec() 得到数据结构: {"网络":"移动4G","机身内存":"32G"}
            使用fastjson 转换为Map,存放在POJO中
             */
            Map<String,String> specMap = JSON.parseObject(item.getSpec(), Map.class);
            item.setSpecMap(specMap);
        }


//        for (TbItem item : list) {
//            Map<String, String> specMap = item.getSpecMap();
//            Set<String> set = specMap.keySet();
//            for (String s : set) {
//                System.out.println(s+"  "+specMap.get(s));
//            }
//        }
        /*
         *统一保存
         */
        solrTemplate.saveBeans(list);
        solrTemplate.commit();
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        ImportItem util = (ImportItem) applicationContext.getBean("importItem");
        util.importAllItem();
        System.out.println("FINISHED!!!");
    }
}
