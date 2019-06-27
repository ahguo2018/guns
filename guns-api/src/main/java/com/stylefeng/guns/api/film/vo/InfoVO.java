package com.stylefeng.guns.api.film.vo;

import com.stylefeng.guns.api.film.vo.ImgVO;
import lombok.Data;

/**
 * @date 2019/6/22 20:24
 */
@Data
public class InfoVO {

    private String filmId;
    private String biography;
    private ActorResponseVO actors;
    private ImgVO imgs;
}
