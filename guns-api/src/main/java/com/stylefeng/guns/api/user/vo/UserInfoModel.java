package com.stylefeng.guns.api.user.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @date 2019/5/27 21:13
 */
@Data
public class UserInfoModel implements Serializable {

    private Integer uuid;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private int sex;
    private String birthday;
    private String lifeState;
    private String biography;
    private String address;
    private String headAddress;
    private long createTime;
    private long updateTime;

}
