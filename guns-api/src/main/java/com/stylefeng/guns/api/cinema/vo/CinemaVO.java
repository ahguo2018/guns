package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2019/6/27 16:15
 */
@Data
public class CinemaVO implements Serializable {
    /** Id */
    private String uuid;
    /** 影院名称 */
    private String cinemaName;
    /** 影院地址 */
    private String address;
    /** 最低票价 */
    private String minimumPrice;

}
