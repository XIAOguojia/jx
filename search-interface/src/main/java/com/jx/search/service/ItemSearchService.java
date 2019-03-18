package com.jx.search.service;

import com.jx.pojo.TbItem;

import java.util.List;
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

    /**
     * 批量导入数据到索引库中
     * @param list 待导入的数据
     */
    void importItemList(List<TbItem> list);

    /**
     * 批量从索引库中删除数据
     * @param goodsIds 待删除的数据的Id
     */
    void deleteByGoodsIds(List goodsIds);
}
