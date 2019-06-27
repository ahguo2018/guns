package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2019/6/10 21:02
 */
@Data
public class SourceVO implements Serializable {

    private String sourceId;
    private String sourceName;
    private boolean isActive;
}
