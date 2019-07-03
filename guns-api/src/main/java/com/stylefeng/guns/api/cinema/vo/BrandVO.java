package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2019/6/27 16:20
 * 品牌
 */
@Data
public class BrandVO implements Serializable {

    /** 品牌ID */
    private String brandId;
    /** 品牌名称 */
    private String brandName;
    /** 是否选中 */
    private boolean isActive;
}
