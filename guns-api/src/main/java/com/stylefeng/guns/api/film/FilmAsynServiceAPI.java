package com.stylefeng.guns.api.film;


import com.stylefeng.guns.api.film.vo.*;

import java.util.List;

/**
 * @date 2019/6/9 11:29
 */
public interface FilmAsynServiceAPI {

    //获取影片相关的其他信息(影片描述/演员信息/图片地址...)
    //影片描述
    FilmDescVO getFilmDesc(String filmId);
    //导演信息
    ActorVO getDirect(String filmId);
    //演员信息
    List<ActorVO> getActors(String filmId);
    //图片地址
    ImgVO getImgs(String filmId);

}
