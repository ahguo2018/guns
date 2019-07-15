package com.stylefeng.guns.api.order;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.order.vo.OrderVO;

import java.util.List;

/**
 * @date 2019/7/4 14:03
 */
public interface OrderServiceAPI {

    //验证售出的票是否为真
    boolean isTrueSeats(Integer fieldId,String seats);

    //已经销售的座位里有没有这些座位
    boolean isNotSoldSeats(Integer fieldId,String seats);

    //创建订单信息
    OrderVO createOrder(Integer userId,Integer fieldId,String soldSeats,String seatsName);

    //使用当前登录人获取已购买的订单
    Page<OrderVO> getOrderByUserId(Integer userId,Page<OrderVO> page);

    //根据fieldId获取所有已销售的座位编号
    String getSoldSeatsByFieldId(Integer fieldId);

}
