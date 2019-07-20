package com.stylefeng.guns.api.alipay.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2019/7/15 13:52
 */
@Data
public class AlipayInfoVO implements Serializable {
    /* 订单ID */
    private String orderId;
    /* 二维码图片地址 */
    private String QRCodeAddress;
}
