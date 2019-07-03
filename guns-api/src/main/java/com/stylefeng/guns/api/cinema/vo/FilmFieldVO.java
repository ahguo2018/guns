package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2019/6/27 23:38
 * 影片放映场次信息
 */
@Data
public class FilmFieldVO implements Serializable {

    /** 影片放映场次ID */
    private String fieldId;
    /** 影片放映开始时间 */
    private String beginTime;
    /** 影片放映结束时间 */
    private String endTime;
    /** 影片语言版本 */
    private String language;
    /** 影厅名称 */
    private String hallName;
    /** 售价 */
    private String price;

}
