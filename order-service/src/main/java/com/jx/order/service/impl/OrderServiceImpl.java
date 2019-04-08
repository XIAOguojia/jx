package com.jx.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jx.grouppojo.Cart;
import com.jx.mapper.TbOrderItemMapper;
import com.jx.mapper.TbPayLogMapper;
import com.jx.order.service.OrderService;
import com.jx.pojo.TbOrderExample;
import com.jx.pojo.TbOrderItem;
import com.jx.pojo.TbPayLog;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jx.mapper.TbOrderMapper;
import com.jx.pojo.TbOrder;

import entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;
import utils.IdWorker;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderMapper orderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private TbOrderItemMapper orderItemMapper;

    @Autowired
    private TbPayLogMapper payLogMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbOrder> findAll() {
        return orderMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbOrder> page = (Page<TbOrder>) orderMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbOrder order) {
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cart").get(order.getUserId());
        List<String> orderList = new ArrayList<>();
        double totalMoney = 0;

        for (Cart cart : cartList) {
            long orderId = idWorker.nextId();
            //新创建订单对象
            TbOrder tbOrder = new TbOrder();
            //订单ID
            tbOrder.setOrderId(orderId);
            //用户名
            tbOrder.setUserId(order.getUserId());
            //状态：未付款
            tbOrder.setStatus("1");
            //支付类型
            tbOrder.setPaymentType(order.getPaymentType());
            //商家ID
            tbOrder.setSellerId(cart.getSellerId());
            //订单创建日期
            tbOrder.setCreateTime(new Date());
            //订单更新日期
            tbOrder.setUpdateTime(new Date());
            //收货人地址
            tbOrder.setReceiverAreaName(order.getReceiverAreaName());
            //收货人手机
            tbOrder.setReceiverMobile(order.getReceiverMobile());
            //收货人
            tbOrder.setReceiver(order.getReceiver());
            //订单来源
            tbOrder.setSourceType(order.getSourceType());
            double money = 0;
            for (TbOrderItem orderItem : cart.getOrderItemList()) {
                //商家ID
                orderItem.setSellerId(cart.getSellerId());
                //订单ID
                orderItem.setOrderId(orderId);
                //明细ID
                orderItem.setId(idWorker.nextId());
                //总金额
                money += orderItem.getTotalFee().doubleValue();
                orderItemMapper.insert(orderItem);
            }

            tbOrder.setPayment(new BigDecimal(money));
            orderMapper.insert(tbOrder);

            //订单列表
            orderList.add(orderId + "");
            //总金额
            totalMoney += money;
        }
        //如果为微信支付
        if ("1".equals(order.getPaymentType())) {
            TbPayLog payLog = new TbPayLog();
            //支付订单号
            String outTradeNo = idWorker.nextId() + "";
            payLog.setOutTradeNo(outTradeNo);
            //创建时间
            payLog.setCreateTime(new Date());
            //总金额 元-》分
            payLog.setTotalFee((long) (totalMoney * 100));
            //订单号列表，逗号分隔
            payLog.setOrderList(orderList.toString().replace("[", "").replace("]", "").replace(" ", ""));
            //支付类型
            payLog.setPayType("1");
            //用户ID
            payLog.setUserId(order.getUserId());
            //插入到支付日志表
            payLogMapper.insert(payLog);
            //放入缓存
            redisTemplate.boundHashOps("payLog").put(order.getUserId(),payLog);
        }

        redisTemplate.boundHashOps("cartList").delete(order.getUserId());
    }


    /**
     * 修改
     */
    @Override
    public void update(TbOrder order) {
        orderMapper.updateByPrimaryKey(order);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbOrder findOne(Long id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            orderMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbOrder order, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbOrderExample example = new TbOrderExample();
        TbOrderExample.Criteria criteria = example.createCriteria();

        if (order != null) {
            if (order.getPaymentType() != null && order.getPaymentType().length() > 0) {
                criteria.andPaymentTypeLike("%" + order.getPaymentType() + "%");
            }
            if (order.getPostFee() != null && order.getPostFee().length() > 0) {
                criteria.andPostFeeLike("%" + order.getPostFee() + "%");
            }
            if (order.getStatus() != null && order.getStatus().length() > 0) {
                criteria.andStatusLike("%" + order.getStatus() + "%");
            }
            if (order.getShippingName() != null && order.getShippingName().length() > 0) {
                criteria.andShippingNameLike("%" + order.getShippingName() + "%");
            }
            if (order.getShippingCode() != null && order.getShippingCode().length() > 0) {
                criteria.andShippingCodeLike("%" + order.getShippingCode() + "%");
            }
            if (order.getUserId() != null && order.getUserId().length() > 0) {
                criteria.andUserIdLike("%" + order.getUserId() + "%");
            }
            if (order.getBuyerMessage() != null && order.getBuyerMessage().length() > 0) {
                criteria.andBuyerMessageLike("%" + order.getBuyerMessage() + "%");
            }
            if (order.getBuyerNick() != null && order.getBuyerNick().length() > 0) {
                criteria.andBuyerNickLike("%" + order.getBuyerNick() + "%");
            }
            if (order.getBuyerRate() != null && order.getBuyerRate().length() > 0) {
                criteria.andBuyerRateLike("%" + order.getBuyerRate() + "%");
            }
            if (order.getReceiverAreaName() != null && order.getReceiverAreaName().length() > 0) {
                criteria.andReceiverAreaNameLike("%" + order.getReceiverAreaName() + "%");
            }
            if (order.getReceiverMobile() != null && order.getReceiverMobile().length() > 0) {
                criteria.andReceiverMobileLike("%" + order.getReceiverMobile() + "%");
            }
            if (order.getReceiverZipCode() != null && order.getReceiverZipCode().length() > 0) {
                criteria.andReceiverZipCodeLike("%" + order.getReceiverZipCode() + "%");
            }
            if (order.getReceiver() != null && order.getReceiver().length() > 0) {
                criteria.andReceiverLike("%" + order.getReceiver() + "%");
            }
            if (order.getInvoiceType() != null && order.getInvoiceType().length() > 0) {
                criteria.andInvoiceTypeLike("%" + order.getInvoiceType() + "%");
            }
            if (order.getSourceType() != null && order.getSourceType().length() > 0) {
                criteria.andSourceTypeLike("%" + order.getSourceType() + "%");
            }
            if (order.getSellerId() != null && order.getSellerId().length() > 0) {
                criteria.andSellerIdLike("%" + order.getSellerId() + "%");
            }

        }

        Page<TbOrder> page = (Page<TbOrder>) orderMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据用户查询payLog
     * @param userId 登录用户
     * @return
     */
    @Override
    public TbPayLog searchPayLogFromRedis(String userId) {
        return  (TbPayLog) redisTemplate.boundHashOps("payLog").get(userId);
    }

    /**
     * 修改订单状态
     * @param out_trade_no 支付订单号
     * @param transaction_id 微信返回的交易流水号
     */
    @Override
    public void updateOrderStatus(String out_trade_no, String transaction_id) {
        //1、修改支付日志装态
        TbPayLog payLog = payLogMapper.selectByPrimaryKey(out_trade_no);
        payLog.setPayTime(new Date());
        payLog.setTransactionId(transaction_id);
        //已支付
        payLog.setTradeState("1");
        payLogMapper.updateByPrimaryKey(payLog);
        //2、修改订单状态
        String orderList = payLog.getOrderList();
        String[] orderIds = orderList.split(",");
        for (String orderId : orderIds) {
            TbOrder order = orderMapper.selectByPrimaryKey(Long.parseLong(orderId));
            if (order!=null){
                order.setUpdateTime(new Date());
                order.setStatus("2");
                orderMapper.updateByPrimaryKey(order);
            }
        }
        //3、从缓存中删除
        redisTemplate.boundHashOps("payLog").delete(payLog.getUserId());
    }

}
