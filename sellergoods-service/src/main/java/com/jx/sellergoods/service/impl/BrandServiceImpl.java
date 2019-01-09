package com.jx.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jx.mapper.TbBrandMapper;
import com.jx.pojo.TbBrand;
import com.jx.pojo.TbBrandExample;
import com.jx.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2018/12/27
 * Time:15:49
 */

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private TbBrandMapper tbBrandMapper;

    @Override
    public List<TbBrand> findAll() {
        return tbBrandMapper.selectByExample(null);
    }

    /**
     * 品牌分页功能
     *
     * @param pageNum  当前页面
     * @param pageSize 每页记录数
     * @return
     */
    @Override
    public PageResult findPages(int pageNum, int pageSize) {
        //分页
        PageHelper.startPage(pageNum, pageSize);
        Page<TbBrand> page = (Page<TbBrand>) tbBrandMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 新增品牌
     *
     * @param brand
     */
    @Override
    public void add(TbBrand brand) throws Exception {
        TbBrandExample example=new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(brand.getName());
        List<TbBrand> tbBrands = tbBrandMapper.selectByExample(example);

        if (tbBrands.size()>0) {
            throw new Exception("已经有这个品牌了");
        } else {
            tbBrandMapper.insert(brand);
        }

    }

    /**
     * 查找实体
     * @param id
     * @return
     */
    @Override
    public TbBrand findOne(Long id) {
        return tbBrandMapper.selectByPrimaryKey(id);
    }

    /**
     * 更新品牌
     * @param brand
     * @throws Exception
     */
    @Override
    public void update(TbBrand brand) throws Exception {
        TbBrandExample example=new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(brand.getName());
        criteria.andFirstCharEqualTo(brand.getFirstChar());
        List<TbBrand> tbBrands = tbBrandMapper.selectByExample(example);
        if (tbBrands.size()>0) {
            throw new Exception("品牌重复");
        } else {
            tbBrandMapper.updateByPrimaryKey(brand);
        }
    }

    /**
     * 删除品牌
     * @param ids
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            tbBrandMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 条件查询
     * @param brand 可能的条件
     * @param pageNum 当前页面
     * @param pageSize 当前页大小
     * @return
     */
    @Override
    public PageResult findPages(TbBrand brand, int pageNum, int pageSize) {
        //分页
        PageHelper.startPage(pageNum, pageSize);
        TbBrandExample example = new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();
        if (brand!=null){
            if (brand.getName()!=null&&brand.getName().trim().length()>0){
                criteria.andNameLike("%"+brand.getName()+"%");
            }
            if (brand.getFirstChar()!=null&&brand.getFirstChar().trim().length()>0){
                criteria.andFirstCharLike("%"+brand.getFirstChar()+"%");
            }
        }
        Page<TbBrand> page = (Page<TbBrand>) tbBrandMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }
}
