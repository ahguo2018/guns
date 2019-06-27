package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2019/6/10 21:00
 *
 */
@Data
public class CatVO implements Serializable {

    private String catId;
    private String catName;
    private boolean isActive;

}
