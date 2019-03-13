package com.jx.search.service;

import java.util.Map;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2019/3/12
 * Time:19:19
 */
public interface ItemSearchService {
    /**
     * 搜索
     * @param searchMap
     * @return
     */
    public Map<String,Object> search(Map searchMap);

}
