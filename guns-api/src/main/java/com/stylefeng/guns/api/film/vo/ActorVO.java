package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2019/6/22 14:20
 */
@Data
public class ActorVO implements Serializable {

    private String filmId;
    private String imgAddress;
    private String directorName;
    private String roleName;

}
