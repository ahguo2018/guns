package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2019/6/21 23:12
 * filmName : ‘动物世界’,
 * 	filmEnName : ‘Animal World’,
 * 	imgAddress : ‘img/films/23412.jpg’,
 * score : ‘8.5’,
 * scoreNum : ‘43万人评分’,
 * totalBox : ‘5.07亿’,
 * info01 : ‘动作，悬疑，冒险’,
 * info02 : ‘中国大陆，美国 / 132分钟’,
 * info03 : ‘2018-06-29大陆上映’,
 */
@Data
public class FilmDetailVO implements Serializable {

    private String filmId;
    private String filmName;
    private String filmEnName;
    private String imgAddress;
    private String score;
    private String scoreNum;
    private String totalBox;
    private String info01;
    private String info02;
    private String info03;

    private InfoVO info04;



}
