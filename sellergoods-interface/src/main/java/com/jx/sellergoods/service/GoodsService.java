package com.jx.sellergoods.service;
import java.util.List;

import com.jx.grouppojo.Goods;
import com.jx.pojo.TbGoods;

import com.jx.pojo.TbItem;
import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface GoodsService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbGoods> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(Goods goods);


	/**
	 * 修改
	 */
	public void update(Goods goods);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public Goods findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize);

    /**
     * 批量修改状态
     * @param ids 要修改的商品ID
     * @param status 更改后的状态
     */
	void updateStatus(Long[] ids,String status);

    /**
     * 上下架商品
     * @param ids 商品ID
     * @param status 更改后的状态
     */
	void upAndDownGoods(Long[] ids,String status) throws Exception;

    /**
     * 根据商品Id和状态查询商品表信息
     * @param goodsIds 商品Id
     * @param status 商品状态（必须为通过审核的）
     * @return
     */
	public List<TbItem> findItemListByGoodsIdandStatus(Long[] goodsIds, String status );
}
