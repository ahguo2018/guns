package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2019/6/27 16:02
 */
@Data
public class CinemaQueryVO implements Serializable {
    /** 影院编号	否,默认为99，全部 */
    private Integer brandId = 99;
    /** 影厅类型	否,默认为99，全部 */
    private Integer hallType = 99;
    /** 行政区编号 否,默认为99，全部 */
    private Integer districtId = 99;
    /** 每页条数	否,默认为12条 */
    private Integer pageSize = 12;
    /** 当前页数	否,默认为第1页 */
    private Integer nowPage = 1;

}
