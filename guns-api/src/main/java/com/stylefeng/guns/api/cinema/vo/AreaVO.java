package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2019/6/27 23:30
 * 行政区域
 */
@Data
public class AreaVO implements Serializable {
        /** 行政区域ID */
        private String areaId;
        /** 行政区域名称 */
        private String areaName;
        /** 是否选中 */
        private boolean isActive;

}
