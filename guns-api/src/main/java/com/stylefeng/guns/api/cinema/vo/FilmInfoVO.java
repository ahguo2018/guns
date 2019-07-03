package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @date 2019/6/27 23:44
 * 影片信息
 */
@Data
public class FilmInfoVO implements Serializable {
    /** 影片Id */
    private String filmId;
    /** 影片名称 */
    private String filmName;
    /** 影片时长 */
    private String filmLength;
    /** 影片类型 */
    private String filmType;
    /** 影片分类 */
    private String filmCats;
    /** 影片演员 */
    private String actors;
    /** 影片图片地址*/
    private String imgAddress;
    /** 影片放映场次信息 */
    private List<FilmFieldVO> filmFields;

}
