package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.stylefeng.guns.api.film.FilmAsynServiceAPI;
import com.stylefeng.guns.api.film.FilmServiceAPI;
import com.stylefeng.guns.api.film.vo.*;
import com.stylefeng.guns.rest.modular.film.vo.FilmConditionVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmIndexVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmRequestVO;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @date 2019/6/8 19:05
 */
@RestController
@RequestMapping("/film")
public class FilmController {

    @Reference(interfaceClass = FilmServiceAPI.class,check = false)
    private FilmServiceAPI filmServiceAPI;
    @Reference(interfaceClass = FilmAsynServiceAPI.class,check = false,async = true)
    private FilmAsynServiceAPI filmAsynServiceAPI;

    private static final String IMG_PRE = "http://img.meetingshop.cn/";

    // 获取首页信息接口
    /*
        API网关：
            1、功能聚合【API聚合】
            好处：
                1、六个接口，一次请求，同一时刻节省了五次HTTP请求
                2、同一个接口对外暴漏，降低了前后端分离开发的难度和复杂度
            坏处：
                1、一次获取数据过多，容易出现问题
     */
    @RequestMapping(value = "/getIndex", method = RequestMethod.GET)
    public ResponseVO getIndex() {
        FilmIndexVO filmIndexVO = new FilmIndexVO();
        //获取banner信息
        List<BannerVO> banners = filmServiceAPI.getBanners();
        filmIndexVO.setBanners(banners);
        //获取正在热映的电影
        filmIndexVO.setHotFilms(filmServiceAPI.getHotFilms(true, 8,1,1,99,99,99));
        //即将上映的电影
        filmIndexVO.setSoonFilms(filmServiceAPI.getSoonFilms(true, 8,1,1,99,99,99));
        //票房排行榜
        filmIndexVO.setBoxRanking(filmServiceAPI.getBoxRanking());
        //获取受欢迎的榜单
        filmIndexVO.setExpectRanking(filmServiceAPI.getExpectRanking());
        //获取前一百
        filmIndexVO.setTop100(filmServiceAPI.getTop());
        return ResponseVO.success(filmIndexVO, IMG_PRE);
    }

    @RequestMapping(value = "/getConditionList", method = RequestMethod.GET)
    public ResponseVO getConditionList(@RequestParam(name = "catId", required = false, defaultValue = "99") String catId,
                                       @RequestParam(name = "sourceId", required = false, defaultValue = "99") String sourceId,
                                       @RequestParam(name = "yearId", required = false, defaultValue = "99") String yearId) {
        FilmConditionVO filmConditionVO = new FilmConditionVO();
        //类型集合
        List<CatVO> catResult = new ArrayList<>();
        List<CatVO> catInfo = filmServiceAPI.getCatInfo();
        for (CatVO catVO : catInfo) {
            if (catVO.getCatId().equals(catId)) {
                catVO.setActive(true);
            } else {
                catVO.setActive(false);
            }
            catResult.add(catVO);
        }
        //区域结合(片源)
        List<SourceVO> sourceResult = new ArrayList<>();
        List<SourceVO> sourceInfo = filmServiceAPI.getSourceInfo();
        for(SourceVO sourceVO : sourceInfo){
            if (sourceVO.getSourceId().equals(sourceId)){
                sourceVO.setActive(true);
            }else {
                sourceVO.setActive(false);
            }
            sourceResult.add(sourceVO);
        }
        //年代集合
        List<YearVO> yearResult = new ArrayList<>();
        List<YearVO> yearInfo = filmServiceAPI.getYearInfo();
        for(YearVO yearVO : yearInfo){
            if (yearVO.getYearId().equals(yearId)){
                yearVO.setActive(true);
            }else {
                yearVO.setActive(false);
            }
            yearResult.add(yearVO);
        }
        filmConditionVO.setCatInfo(catResult);
        filmConditionVO.setSourceInfo(sourceResult);
        filmConditionVO.setYearInfo(yearResult);
        return ResponseVO.success(filmConditionVO);
    }


    @RequestMapping(value = "/getFilms",method = RequestMethod.GET)
    public ResponseVO getFilms(FilmRequestVO filmRequestVO){
        String img_pre = "http://img.meetingshop.cn/";
        FilmVO filmVO = null;
        switch (filmRequestVO.getShowType()){
            case 1:
                filmVO = filmServiceAPI.getHotFilms(false,filmRequestVO.getPageSize(),filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(),filmRequestVO.getCatId(),filmRequestVO.getSourceId(),filmRequestVO.getYearId());
                break;
            case 2:
                filmVO = filmServiceAPI.getSoonFilms(false,filmRequestVO.getPageSize(),filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(),filmRequestVO.getCatId(),filmRequestVO.getSourceId(),filmRequestVO.getYearId());
                break;
            case 3:
                filmVO = filmServiceAPI.getClassicFilms(filmRequestVO.getPageSize(),filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(),filmRequestVO.getCatId(),filmRequestVO.getSourceId(),filmRequestVO.getYearId());
                break;
            default:
                filmVO = filmServiceAPI.getSoonFilms(false,filmRequestVO.getPageSize(),filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(),filmRequestVO.getCatId(),filmRequestVO.getSourceId(),filmRequestVO.getYearId());
                break;
        }
        return ResponseVO.success(filmVO.getFilmInfo(),img_pre,filmRequestVO.getNowPage(),filmVO.getTotalPage());
    }



    @GetMapping(value = "/films/{searchParam}")
    public ResponseVO films(@PathVariable("searchParam")String searchParam,int searchType) throws ExecutionException, InterruptedException {
        //searchType: ‘0表示按照编号查找，1表示按照名称查找’
        //根据searchType,判断查询类型
        //不同的查询类型,传入的条件会略有不同
        FilmDetailVO filmDetail = filmServiceAPI.getFilmDetail(searchType, searchParam);

        //查询影片的详细信息 -->dubbo的异步调用
        if (filmDetail!=null){
            String filmId = filmDetail.getFilmId();
            //影片描述
            filmAsynServiceAPI.getFilmDesc(filmId);
            Future<FilmDescVO> filmDescVOFuture = RpcContext.getContext().getFuture();
            //图片地址
            filmAsynServiceAPI.getImgs(filmId);
            Future<ImgVO> imgVOFuture = RpcContext.getContext().getFuture();
            //导演信息
            filmAsynServiceAPI.getDirect(filmId);
            Future<ActorVO> actorVOFuture = RpcContext.getContext().getFuture();
            //演员信息
            filmAsynServiceAPI.getActors(filmId);
            Future<List<ActorVO>> actorsVOFuture = RpcContext.getContext().getFuture();

            InfoVO infoVO = new InfoVO();
            infoVO.setBiography(filmDescVOFuture.get().getBiography());
            infoVO.setFilmId(filmId);
            infoVO.setImgs(imgVOFuture.get());
            ActorResponseVO actorResponseVO = new ActorResponseVO();
            actorResponseVO.setDirector(actorVOFuture.get());
            actorResponseVO.setActors(actorsVOFuture.get());
            infoVO.setActors(actorResponseVO);
            filmDetail.setInfo04(infoVO);
            return ResponseVO.success(filmDetail,"http://img.meetingshop.cn/");
        }else {
            return ResponseVO.serviceFail("无该影片信息");
        }
    }
}
