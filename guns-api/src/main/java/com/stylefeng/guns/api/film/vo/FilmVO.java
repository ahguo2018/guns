package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @date 2019/6/8 21:38
 */
@Data
public class FilmVO implements Serializable {

    private int filmNum;
    private List<FilmInfo> filmInfo;
    private int nowPage;
    private int totalPage;
}
