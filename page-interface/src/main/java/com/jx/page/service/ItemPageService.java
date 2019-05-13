package com.jx.page.service;

import java.util.Map;

/**
 * Created by intellij IDEA
 * Author:Raven
 * Date:2019/3/20
 * Time:16:20
 * 商品详细也接口
 */
public interface ItemPageService {
    /**
     * 生成商品详细页
     * @param goodsId
     */
    public boolean genItemHtml(Long goodsId);

    /**
     * 删除商品详细页
     * @param goodsIds
     * @return
     */
    public boolean deleteItemHtml(Long[] goodsIds);


}
