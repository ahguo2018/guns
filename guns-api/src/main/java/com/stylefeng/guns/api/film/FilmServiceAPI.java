package com.stylefeng.guns.api.film;


import com.stylefeng.guns.api.film.vo.*;

import java.util.List;

/**
 * @date 2019/6/9 11:29
 */
public interface FilmServiceAPI {

    //获取banners
    List<BannerVO> getBanners();
    //获取热映影片
    FilmVO getHotFilms(boolean isLimit,int nums,int nowPage,int sortId,int catId,int sourceId,int yearId);
    //获取即将上映影片
    FilmVO getSoonFilms(boolean isLimit,int nums,int nowPage,int sortId,int catId,int sourceId,int yearId);
    //获取经典影片
    FilmVO getClassicFilms(int nums,int nowPage,int sortId,int catId,int sourceId,int yearId);
    //获取票房排行榜
    List<FilmInfo> getBoxRanking();
    //获取人气排行榜
    List<FilmInfo> getExpectRanking();
    //获取top
    List<FilmInfo> getTop();

    //获取影片条件
    //获取类型条件
    List<CatVO> getCatInfo();
    //获取区域(片源)条件
    List<SourceVO> getSourceInfo();
    //获取年代条件
    List<YearVO> getYearInfo();

    //根据影片ID或者名称获取影片信息
    FilmDetailVO getFilmDetail(int searchType,String searchParam);

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
