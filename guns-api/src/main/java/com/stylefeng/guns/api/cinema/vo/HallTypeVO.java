package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2019/6/27 23:30
 * 影厅类型
 */
@Data
public class HallTypeVO implements Serializable {
        /** 影厅类型ID */
        private String halltypeId;
        /** 影厅类型名称 */
        private String halltypeName;
        /** 是否选中 */
        private boolean isActive;

}
