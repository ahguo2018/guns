package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2019/6/27 23:34
 * 影院信息
 */
@Data
public class CinemaInfoVO implements Serializable {
    /** 影院编号*/
    private String cinemaId;
    /** 影院图片url */
    private String imgUrl;
    /** 影院名称*/
    private String cinemaName;
    /** 影院地址 */
    private String cinemaAdress;
    /** 影院电话 */
    private String cinemaPhone;

}
