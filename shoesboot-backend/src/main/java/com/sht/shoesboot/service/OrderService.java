package com.sht.shoesboot.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sht.shoesboot.DTO.OrderDTO;
import com.sht.shoesboot.entity.Order;
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
        String inventory = redisService.getData("shoes_goods_" + order.getGoodsId());
        if (StringUtils.isNotEmpty(inventory)) {
            if (Integer.parseInt(inventory) >= order.getAmount()) {
                order.setOrderNumber(OrderNumber.createOrderNumber());
                order.setInDate(new Date());
                try {
                    orderMapper.insertSelective(order);
                } catch (Exception e) {
                    return new RestResponse(400, "创建订单失败,请重试");
                }
                //删除购物车
                if (order.getCartId() != null && order.getCartId() != 0) {
                    shopCartMapper.deleteByPrimaryKey(order.getCartId());
                }
                //更新redis库存
                redisService.setData("shoes_goods_" + order.getGoodsId(), (Integer.parseInt(inventory) - order.getAmount()) + "");
                //更新数据库库存
                goodsMapper.updateInventory(order.getGoodsId(), order.getAmount());
                return new RestResponse(200, "成功");
            } else {
                return new RestResponse(400, "库存不足");
            }
        }
        return new RestResponse(400, "该商品已下架");
    }

    public PageResult<OrderDTO> queryPage(String status, Integer userId, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<OrderDTO> dtoList = orderMapper.queryPage(status, userId);
        Page<OrderDTO> dtoPage = (Page<OrderDTO>) dtoList;
        return new PageResult<>(dtoPage.getTotal(), dtoPage.getPages(), dtoPage.getResult());
    }
}
