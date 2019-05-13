package com.jx.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.jx.search.service.ItemSearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2019/3/12
 * Time:19:44
 */
@RestController
@RequestMapping("/itemsearch")
public class ItemSearchController {
    @Reference
    private ItemSearchService itemSearchService;

    @RequestMapping("/search")
    public Map<String, Object> search(@RequestBody Map searchMap ){
        System.out.println(searchMap.get("keywords"));
        //        System.out.println(JSON.toJSONString(searchMap));
        System.out.println(searchMap.toString());
        return  itemSearchService.search(searchMap);
    }

    @RequestMapping("test")
    public String test(@RequestBody Map map){
        return "test";
    }

}

