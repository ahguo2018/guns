package com.stylefeng.guns.rest.modular.cinema.vo;

import com.stylefeng.guns.api.cinema.vo.CinemaVO;
import lombok.Data;

import java.util.List;

/**
 * @date 2019/7/1 23:21
 */
@Data
public class CinemaListResposeVO {

    private List<CinemaVO> cinemas;
}
