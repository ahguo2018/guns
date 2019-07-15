package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2019/7/6 15:27
 */
@Data
public class OrderQueryVO implements Serializable {

    private String cinemaId;
    private String filmId;
    private String film_price;
}
