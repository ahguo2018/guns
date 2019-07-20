package com.stylefeng.guns.api.alipay;

import com.stylefeng.guns.api.alipay.vo.AlipayInfoVO;
import com.stylefeng.guns.api.alipay.vo.AlipayResultVO;

/**
 * @date 2019/7/15 13:51
 */
public interface AlipayServiceAPI {

    AlipayInfoVO getQRCode(String orderId);

    AlipayResultVO getOrderStatus(String orderId);
}
