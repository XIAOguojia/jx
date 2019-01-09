package com.jx.sellergoods.service;

import com.jx.pojo.TbBrand;
import entity.PageResult;

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

    /**
     * 返回分页列表
     * @param pageNum 当前页面
     * @param pageSize 每页记录数
     * @return
     */
    PageResult findPages(int pageNum,int pageSize);

    /**
     * 新增品牌
     * @param brand
     */
    void add(TbBrand brand) throws Exception;

    /**
     * 修改品牌第一步 查找
     * @param id
     * @return
     */
    TbBrand findOne(Long id);

    /**
     * 修改品牌第二步 更新
     * @param brand
     */
    void update(TbBrand brand) throws Exception;

    /**
     * 删除
     * @param ids
     */
    void delete(Long[] ids);

    /**
     * 按条件查询
     * @param brand 可能的条件
     * @param pageNum 当前页面
     * @param pageSize 当前页大小
     * @return
     */
    PageResult findPages(TbBrand brand,int pageNum,int pageSize);
}
