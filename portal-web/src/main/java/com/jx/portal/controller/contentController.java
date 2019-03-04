package com.jx.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jx.content.service.ContentService;
import com.jx.pojo.TbContent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2019/3/4
 * Time:10:09
 */
@RestController
@RequestMapping("/content")
public class contentController {
    @Reference
    private ContentService contentService;

    @RequestMapping("/findByCategoryId")
    public List<TbContent> findByCategoryId(Long categoryId){
        return contentService.findByCategoryId(categoryId);
    }
}
