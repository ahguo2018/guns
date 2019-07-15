package com.stylefeng.guns.rest.modular.order.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.OrderQueryVO;
import com.stylefeng.guns.api.order.OrderServiceAPI;
import com.stylefeng.guns.api.order.vo.OrderVO;
import com.stylefeng.guns.core.util.UUIDUtil;
import com.stylefeng.guns.rest.common.persistence.dao.MoocOrder2017TMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MoocOrderTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocOrder2017T;
import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import com.stylefeng.guns.rest.common.util.FtpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @date 2019/7/4 15:57
 */
@Slf4j
@Component
@Service(interfaceClass = OrderServiceAPI.class,group = "order2017")
public class OrderServiceImpl2017 implements OrderServiceAPI {

    @Autowired
    private MoocOrder2017TMapper moocOrderTMapper;

    @Autowired
    private FtpUtil ftpUtil;

    @Reference(interfaceClass = CinemaServiceAPI.class,check = false)
    private CinemaServiceAPI cinemaServiceAPI;

    @Override
    public boolean isTrueSeats(Integer fieldId, String seats) {
        //根据fieldId找到座位位置图
        String seatPath = moocOrderTMapper.getSeatAddressByFieldId(fieldId);
        //读取位置图
        String str = ftpUtil.getFileStrByPath(seatPath);
        //转换为JsonObject
        JSONObject jsonObject = JSONObject.parseObject(str);
        String ids = (String) jsonObject.get("ids");
        //判断是否匹配
        String[] idsArray = ids.split(",");
        String[] seatsArray = seats.split(",");
        boolean subFlag = isSub(idsArray, seatsArray);
        return subFlag;
    }

    private boolean isSub(String[] idsArray,String[] seatsArray){
        List<String> idsList = Arrays.asList(idsArray);
        List<String> seatsList = Arrays.asList(seatsArray);
        if (idsList.containsAll(seatsList)){
            return true;
        }
        return false;
    }

    @Override
    public boolean isNotSoldSeats(Integer fieldId, String seats) {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("field_id",fieldId);
        List<MoocOrder2017T> list = moocOrderTMapper.selectList(entityWrapper);
        String[] seatsIdsArrs = seats.split(",");
        //有任何一个编号匹配上,则返回失败
        for (MoocOrder2017T moocOrderT : list){
            String[] soldSeatsIdsArrs = moocOrderT.getSeatsIds().split(",");
            for (String soldSeatIdsArr : soldSeatsIdsArrs){
                for (String seatsIdArr : seatsIdsArrs){
                    if (seatsIdArr.equalsIgnoreCase(soldSeatIdsArr)){
                        return false;
                    }
                }
            }
        }
        //TODO 待改造--二分法算法
        return true;
    }

    @Override
    public OrderVO createOrder(Integer userId, Integer fieldId, String soldSeats, String seatsName) {
        //编号
        String uuid = UUIDUtil.generateUuid();
        //获取cinemaId/filmId/filmPrice
        OrderQueryVO orderNeeds = cinemaServiceAPI.getOrderNeeds(fieldId);
        Integer cinemaId = Integer.parseInt(orderNeeds.getCinemaId());
        Integer filmId = Integer.parseInt(orderNeeds.getFilmId());
        Double filmPrice = Double.parseDouble(orderNeeds.getFilm_price());
        //计算orderPrice
        int solds = soldSeats.split(",").length;
        double orderPrice = getOrderPrice(solds, filmPrice);
        //创建订单
        MoocOrder2017T moocOrderT = new MoocOrder2017T();
        moocOrderT.setUuid(uuid);
        moocOrderT.setCinemaId(cinemaId);
        moocOrderT.setFieldId(fieldId);
        moocOrderT.setFilmId(filmId);
        moocOrderT.setFilmPrice(filmPrice);
        moocOrderT.setOrderPrice(orderPrice);
        moocOrderT.setSeatsIds(soldSeats);
        moocOrderT.setSeatsName(seatsName);
        moocOrderT.setOrderUser(userId);

        Integer insert = moocOrderTMapper.insert(moocOrderT);
        if (insert>0){
            //返回查询结果
            OrderVO orderVO = moocOrderTMapper.getOrderInfoById(uuid);
            if (orderVO==null || StringUtils.isEmpty(orderVO.getOrderId())){
                log.error("订单信息查询失败,订单编号为{}",uuid);
                return null;
            }else {
                return orderVO;
            }
        }else {
            //插入失败
            log.error("订单插入失败");
            return null;
        }
    }

    private double getOrderPrice(int solds,double filmPrice){
        BigDecimal orderPrice = new BigDecimal(solds).multiply(new BigDecimal(filmPrice))
                .setScale(2,RoundingMode.HALF_UP);
        return orderPrice.doubleValue();
    }


    @Override
    public Page<OrderVO> getOrderByUserId(Integer userId,Page<OrderVO> page) {
        Page<OrderVO> result = new Page<>();
        if (userId==null){
            log.error("订单查询业务失败，用户编号未传入");
            return null;
        }
        List<OrderVO> orderVOList = moocOrderTMapper.getOrderInfoByUserId(userId,page);
        if (CollectionUtils.isEmpty(orderVOList)){
            result.setTotal(0);
            result.setRecords(new ArrayList<>());
            return result;
        }else {
            // 获取订单总数
            EntityWrapper<MoocOrder2017T> entityWrapper = new EntityWrapper<>();
            entityWrapper.eq("order_user",userId);
            Integer count = moocOrderTMapper.selectCount(entityWrapper);
            //将结果放入Page
            result.setTotal(count);
            result.setRecords(orderVOList);
            return result;
        }
    }

    @Override
    public String getSoldSeatsByFieldId(Integer fieldId) {
        if(fieldId==null){
            log.error("查询已售座位错误，未传入任何场次编号");
            return "";
        }else{
            String soldSeats = moocOrderTMapper.getSoldSeatsByFieldId(fieldId);
            return soldSeats;
        }

    }
}
