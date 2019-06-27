package com.stylefeng.guns.api.film.vo;

import com.stylefeng.guns.api.film.vo.ActorVO;
import lombok.Data;

import java.util.List;

/**
 * @date 2019/6/22 20:14
 */
@Data
public class ActorResponseVO {

    private ActorVO director;
    private List<ActorVO> actors;
}
