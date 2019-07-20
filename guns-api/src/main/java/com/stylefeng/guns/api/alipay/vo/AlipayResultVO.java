package com.stylefeng.guns.api.alipay.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2019/7/15 14:16
 *
 */
@Data
public class AlipayResultVO implements Serializable {
    /* 订单ID */
    private String orderId;
    /* 订单支付状态 */
    private Integer orderStatus;
    /* 订单支付情况 */
    private String orderMsg;
}
