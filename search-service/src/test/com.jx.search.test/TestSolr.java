import com.jx.pojo.TbItem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;

import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2019/3/8
 * Time:10:19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-solr.xml")
public class TestSolr {
    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void test(){
        String s = "aca";
        System.out.println(s.getClass().getSimpleName());
    }

    @Test
    public void testAdd() {
        TbItem item = new TbItem();
        item.setId(1L);
        item.setBrand("华为");
        item.setCategory("手机");
        item.setGoodsId(1L);
        item.setSeller("华为2号专卖店");
        item.setTitle("华为Mate9");
        item.setPrice(new BigDecimal(2000));
        //保存
        solrTemplate.saveBean(item);
        //提交
        solrTemplate.commit();

    }

    @Test
    public void testGetById() {
        TbItem item = solrTemplate.getById(1, TbItem.class);
        System.out.println(item.getPrice());
        System.out.println(item);
    }

    @Test
    public void testDelete() {
        solrTemplate.deleteById("1");
        solrTemplate.commit();
    }

    @Test
    public void testAddList() {
        List<TbItem> list = new ArrayList();

        for (int i = 0; i < 100; i++) {
            TbItem item = new TbItem();
            item.setId(i + 1L);
            item.setBrand("华为");
            item.setCategory("手机");
            item.setGoodsId(1L);
            item.setSeller("华为2号专卖店");
            item.setTitle("华为Mate" + i);
            item.setPrice(new BigDecimal(2000 + i));
            list.add(item);
        }

        solrTemplate.saveBeans(list);
        solrTemplate.commit();
    }

    @Test
    public void testPageQuery() {
        Query query = new SimpleQuery("item_spec_网络:移动4G");
        //开始索引（默认为0）
        query.setOffset(20);
        //设置每页条数（默认为10）
        query.setRows(20);
        ScoredPage<TbItem> items = solrTemplate.queryForPage(query, TbItem.class);
        System.out.println("总记录数：" + items.getTotalElements());
        List<TbItem> itemList = items.getContent();
        showList(itemList);
    }


    private void showList(List<TbItem> list) {
        for (TbItem item : list) {
            System.out.println(item.getTitle() + "  " + item.getPrice());
        }
    }

    @Test
    public void testPageQueryMutil() {
        Query query = new SimpleQuery("*:*");
//        Criteria criteria = new Criteria("item_title").contains("2");
//        criteria = criteria.and("item_title").contains("5");
        Criteria criteria = new Criteria("item_brand").contains("华为");
        query.addCriteria(criteria);
        ScoredPage<TbItem> tbItems = solrTemplate.queryForPage(query, TbItem.class);
        List<TbItem> items = tbItems.getContent();
        showList(items);
    }

    @Test
    public void testdeleteAll(){
        Query query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

    @Test
    public void testSearch(){
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
