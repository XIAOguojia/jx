package com.jx.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jx.pojo.TbBrand;
import com.jx.sellergoods.service.BrandService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2018/12/27
 * Time:15:54
 */
@RestController
@RequestMapping("/brand")
public class BrandController {
    @Reference
    private BrandService brandService;

    @RequestMapping("/findAll.do")
    public List<TbBrand> findAll(){
        return brandService.findAll();
    }

    @RequestMapping("/test.do")
    public String test(){
        System.out.println(brandService);
        return "TEST!!!!";
    }
}
