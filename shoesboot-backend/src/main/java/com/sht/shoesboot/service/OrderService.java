package com.sht.shoesboot.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sht.shoesboot.DTO.Delivery;
import com.sht.shoesboot.DTO.OrderDTO;
import com.sht.shoesboot.entity.Order;
import com.sht.shoesboot.entity.OrderGoods;
import com.sht.shoesboot.entity.PageResult;
import com.sht.shoesboot.mapper.GoodsMapper;
import com.sht.shoesboot.mapper.OrderMapper;
import com.sht.shoesboot.mapper.ShopCartMapper;
import com.sht.shoesboot.utils.OrderNumber;
import com.sht.shoesboot.utils.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.List;

/**
 * @author Aaron
 * @date 2020/12/29 22:53
 */
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ShopCartMapper shopCartMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Transactional
    public synchronized RestResponse createOrder(Order order) {
        for (OrderGoods orderGoods : order.getOrderGoodsList()) {
            String inventory = redisService.getData("shoes_goods_" + orderGoods.getGoodsId());
            if (StringUtils.isNotEmpty(inventory)) {
                if (Integer.parseInt(inventory) >= orderGoods.getAmount()) {
                    //删除购物车
                    if (orderGoods.getCartId() != null && orderGoods.getCartId() != 0) {
                        shopCartMapper.deleteByPrimaryKey(orderGoods.getCartId());
                    }
                    //更新redis库存
                    redisService.setData("shoes_goods_" + orderGoods.getGoodsId(), (Integer.parseInt(inventory) - orderGoods.getAmount()) + "");
                    //更新数据库库存
                    goodsMapper.updateInventory(orderGoods.getGoodsId(), orderGoods.getAmount());
                } else {
                    return new RestResponse(400, orderGoods.getTitle() + "，库存不足");
                }
            }else {
                return new RestResponse(400, orderGoods.getTitle() + "该商品已下架");
            }
        }
        try {
            order.setOrderNumber(OrderNumber.createOrderNumber());
            order.setInDate(new Date());
            order.setStatus("C");
            orderMapper.insertSelective(order);
            order.getOrderGoodsList().forEach(item -> item.setOrderId(order.getId()));
            orderMapper.batchInsert(order.getOrderGoodsList());
            return new RestResponse(200, "下单成功");
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new RestResponse(400, "创建订单失败,请重试");
        }
    }

    public PageResult<Order> queryPage(String status, Integer userId, Integer id, Integer page, Integer size) {
        if ("".equals(status)) {
            status = null;
        }
        PageHelper.startPage(page, size);
        List<Order> dtoList = orderMapper.queryPage(status, userId, id);
        Page<Order> dtoPage = (Page<Order>) dtoList;
        return new PageResult<>(dtoPage.getTotal(), dtoPage.getPages(), dtoPage.getResult());
    }

    public boolean delete(Integer id) {
        try {
            orderMapper.deleteByPrimaryKey(id);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public RestResponse delivery(Delivery delivery) {
        if (orderMapper.existsWithPrimaryKey(delivery.getId())){
            Order order = new Order();
            order.setId(delivery.getId());
            order.setExpress(delivery.getExpress());
            order.setExpressNum(delivery.getExpressNum());
            order.setStatus(delivery.getStatus());
            try {
                orderMapper.updateByPrimaryKeySelective(order);
                return new RestResponse(200, "发货成功");
            }catch (Exception e){
                return new RestResponse(400, e.getMessage(), "填写失败，请稍后重试");
            }
        }
        return new RestResponse(400, "不存在该订单");
    }
}
