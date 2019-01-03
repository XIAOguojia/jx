package com.jx.sellergoods.service;

import com.jx.pojo.TbBrand;

import java.util.List;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2018/12/27
 * Time:15:45
 * 品牌列表
 */
public interface BrandService {
    List<TbBrand> findAll();
}
