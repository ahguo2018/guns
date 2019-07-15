package com.stylefeng.guns.rest.modular.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.util.concurrent.RateLimiter;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.stylefeng.guns.api.order.OrderServiceAPI;
import com.stylefeng.guns.api.order.vo.OrderVO;
import com.stylefeng.guns.rest.common.CurrentUser;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2019/7/3 17:43
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference(interfaceClass = OrderServiceAPI.class,group = "order2017",check = false)
    private OrderServiceAPI orderServiceAPI2017;

    @Reference(interfaceClass = OrderServiceAPI.class,group = "order2018",check = false)
    private OrderServiceAPI orderServiceAPI2018;

    @Reference(interfaceClass = OrderServiceAPI.class,group = "order2019",check = false)
    private OrderServiceAPI orderServiceAPI;
    //创建一个限流器，参数代表每秒生成的令牌数
    private static RateLimiter rateLimiter = RateLimiter.create(2);



    public ResponseVO error(Integer fieldId,String soldSeats,String seatsName){
        return ResponseVO.serviceFail("抱歉，下单的人太多了，请稍后重试");
    }

    /**
     * 用户下单购票接口
     * @param fieldId 场次编号
     * @param soldSeats 购买座位编号
     * @param seatsName 购买座位的名称
     * @return
     */
    /*
        信号量隔离
        线程池隔离
        线程切换
     */
    @HystrixCommand(fallbackMethod = "error", commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "4000"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")},
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "1"),
                    @HystrixProperty(name = "maxQueueSize", value = "10"),
                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "1000"),
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "8"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "12"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "1500")}
    )
    @PostMapping(value = "/buyTickets")
    public ResponseVO buyTickets(Integer fieldId,String soldSeats,String seatsName){
        //tryAcquire(int permits, long timeout, TimeUnit unit)来设置等待超时时间的方式获取令牌，
        // 如果超timeout为0，则代表非阻塞，获取不到立即返回。
        if (rateLimiter.tryAcquire()){
            try{
                //验证售出的票是否为真
                boolean trueSeats = orderServiceAPI.isTrueSeats(fieldId, soldSeats);
                //已经销售的座位里有没有这些座位
                boolean notSoldSeats = orderServiceAPI.isNotSoldSeats(fieldId, soldSeats);
                // 验证，上述两个内容有一个不为真，则不创建订单信息
                if (trueSeats&&notSoldSeats){
                    //获取当前登录人
                    String userId = CurrentUser.getCurrentUser();
                    if (StringUtils.isBlank(userId)){
                        return ResponseVO.serviceFail("用户未登录");
                    }
                    //创建订单信息
                    OrderVO orderVO = orderServiceAPI.createOrder(Integer.parseInt(userId), fieldId, soldSeats, seatsName);
                    if (orderVO==null){
                        log.error("购票失败");
                        return ResponseVO.serviceFail("购票失败");
                    }else {
                        return ResponseVO.success(orderVO);
                    }
                }else {
                    log.error("订单中的座位编号有问题");
                    return ResponseVO.serviceFail("订单中的座位编号有问题");
                }
            }catch (Exception e){
                log.error("购票业务异常",e);
                return ResponseVO.serviceFail("购票业务异常");
            }
        }else {
            return ResponseVO.serviceFail("购票人数过多，请稍后再试");
        }
    }

    /**
     * 获取用户订单信息接口
     * @param nowPage 当前页
     * @param pageSize 每页多少条
     * @return
     */
    @PostMapping(value = "/getOrderInfo")
    public ResponseVO getOrderInfo(
            @RequestParam(name = "nowPage",required = false,defaultValue = "1") Integer nowPage,
            @RequestParam(name = "pageSize",required = false,defaultValue = "5")Integer pageSize){
        //获取当前登录人的信息
        String userId = CurrentUser.getCurrentUser();
        //使用当前登录人获取已购买的订单
        if (StringUtils.isBlank(userId)){
            return ResponseVO.serviceFail("用户未登录");
        }
        Page<OrderVO> page = new Page<>(nowPage,pageSize);
        Page<OrderVO> result = orderServiceAPI.getOrderByUserId(Integer.parseInt(userId), page);
        Page<OrderVO> result2017 = orderServiceAPI2017.getOrderByUserId(Integer.parseInt(userId), page);
        Page<OrderVO> result2018 = orderServiceAPI2018.getOrderByUserId(Integer.parseInt(userId), page);
        //合并结果
        int totalPages = (int) (result.getPages()+ result2017.getPages()+result2018.getPages());
        //订单合并
        List<OrderVO> orderVOList = new ArrayList<>();
        orderVOList.addAll(result.getRecords());
        orderVOList.addAll(result2017.getRecords());
        orderVOList.addAll(result2018.getRecords());
        return ResponseVO.success(orderVOList,nowPage,totalPages);
    }


    public static void main(String[] args) {
        for (int i=1;i<10;i=i+2){
            //通过limiter.acquire(i);来以阻塞的方式获取令牌
            double waitTime = rateLimiter.acquire(i);
            System.out.println("cutTime=" + System.currentTimeMillis() + " acq:" + i + " waitTime:" + waitTime);
        }
    }

}
