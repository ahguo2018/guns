package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2019/6/10 21:03
 */
@Data
public class YearVO implements Serializable {

    private String yearId;
    private String yearName;
    private boolean isActive;

}
