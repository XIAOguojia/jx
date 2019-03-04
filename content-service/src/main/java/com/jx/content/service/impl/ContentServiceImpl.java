package com.jx.content.service.impl;

import java.util.List;

import com.jx.content.service.ContentService;
import com.jx.pojo.TbContentExample;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jx.mapper.TbContentMapper;
import com.jx.pojo.TbContent;


import entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbContent> findAll() {
        return contentMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbContent content) {
        contentMapper.insert(content);
        //清楚缓存
        redisTemplate.boundHashOps("content").delete(content.getCategoryId());
    }


    /**
     * 修改
     */
    @Override
    public void update(TbContent content) {
        Long categoryId = contentMapper.selectByPrimaryKey(content.getId()).getCategoryId();
        //清楚更新前的缓存
        redisTemplate.boundHashOps("content").delete(categoryId);

        contentMapper.updateByPrimaryKey(content);
        //判断更新前后的分类ID是否相同
        if (!categoryId.equals(content.getCategoryId())) {
            //清楚更新后的缓存
            redisTemplate.boundHashOps("content").delete(content.getCategoryId());
        }
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbContent findOne(Long id) {
        return contentMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            //删除前清空缓存，否则会无法删除
            Long categoryId = contentMapper.selectByPrimaryKey(id).getCategoryId();
            redisTemplate.boundHashOps("content").delete(categoryId);

            contentMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbContent content, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();

        if (content != null) {
            if (content.getTitle() != null && content.getTitle().length() > 0) {
                criteria.andTitleLike("%" + content.getTitle() + "%");
            }
            if (content.getUrl() != null && content.getUrl().length() > 0) {
                criteria.andUrlLike("%" + content.getUrl() + "%");
            }
            if (content.getPic() != null && content.getPic().length() > 0) {
                criteria.andPicLike("%" + content.getPic() + "%");
            }
            if (content.getContent() != null && content.getContent().length() > 0) {
                criteria.andContentLike("%" + content.getContent() + "%");
            }
            if (content.getStatus() != null && content.getStatus().length() > 0) {
                criteria.andStatusLike("%" + content.getStatus() + "%");
            }

        }

        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据广告类型ID查询列表
     *
     * @param categoryId 分类ID
     * @return
     */
    @Override
    public List<TbContent> findByCategoryId(Long categoryId) {
        List<TbContent> list = (List<TbContent>) redisTemplate.boundHashOps("content").get(categoryId);
        if (list == null) {
            TbContentExample example = new TbContentExample();
            TbContentExample.Criteria criteria = example.createCriteria();
            //分类ID
            criteria.andCategoryIdEqualTo(categoryId);
            //状态必须有效
            criteria.andStatusEqualTo("1");
            //排序
            example.setOrderByClause("sort_order");
            list = contentMapper.selectByExample(example);
            //存入缓存
            redisTemplate.boundHashOps("content").put(categoryId, list);
            System.out.println("从数据库中读取");
        } else {
            System.out.println("从缓存中读取");
        }
        return list;
    }

}
