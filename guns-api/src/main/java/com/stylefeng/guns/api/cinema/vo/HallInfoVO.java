package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2019/6/27 23:50
 * 影厅信息
 * 						"hallFieldId":"1",
 * 						"hallName":"1号VIP厅",
 * 						"price":48,
 * 						"seatFile":"halls/4552.json",
 * 						"soldSeats":"1,2,3,5,12"  -> 假的，因为没有订单
 */
@Data
public class HallInfoVO implements Serializable {

    /** 影厅ID */
    private String hallFieldId;
    /** 影厅名称 */
    private String hallName;
    /** 票价 */
    private String price;
    /** 影厅座位 */
    private String seatFile;
    /** 影厅已售出座位 必须关联订单才能查询*/
    private String soldSeats;

}
