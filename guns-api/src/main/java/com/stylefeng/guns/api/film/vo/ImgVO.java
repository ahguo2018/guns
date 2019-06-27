package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2019/6/22 14:18
 */
@Data
public class ImgVO implements Serializable {

    private String filmId;
    private String mainImg;
    private String img01;
    private String img02;
    private String img03;
    private String img04;
}
