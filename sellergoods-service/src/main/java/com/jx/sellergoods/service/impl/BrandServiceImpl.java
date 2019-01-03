package com.jx.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.jx.mapper.TbBrandMapper;
import com.jx.pojo.TbBrand;
import com.jx.sellergoods.service.BrandService;
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
}
