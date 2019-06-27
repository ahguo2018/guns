package com.stylefeng.guns.rest.modular.film.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.film.FilmServiceAPI;
import com.stylefeng.guns.api.film.vo.*;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2019/6/9 12:03
 */
@Component
@Service(interfaceClass = FilmServiceAPI.class)
public class FilmServiceImpl implements FilmServiceAPI {

    @Autowired
    private MoocBannerTMapper moocBannerTMapper;
    @Autowired
    private MoocFilmTMapper moocFilmTMapper;
    @Autowired
    private MoocCatDictTMapper moocCatDictTMapper;
    @Autowired
    private MoocSourceDictTMapper moocSourceDictTMapper;
    @Autowired
    private MoocYearDictTMapper moocYearDictTMapper;
    @Autowired
    private MoocFilmInfoTMapper moocFilmInfoTMapper;
    @Autowired
    private MoocActorTMapper moocActorTMapper;
    @Autowired
    private MoocFilmActorTMapper moocFilmActorTMapper;

    @Override
    public List<BannerVO> getBanners() {
        List<BannerVO> bannerVOS = new ArrayList<>();
        List<MoocBannerT> moocBannerTS = moocBannerTMapper.selectList(null);
        for (MoocBannerT moocBannerT : moocBannerTS){
            BannerVO bannerVO = new BannerVO();
            bannerVO.setBannerId(moocBannerT.getUuid()+"");
            bannerVO.setBannerAddress(moocBannerT.getBannerAddress());
            bannerVO.setBannerUrl(moocBannerT.getBannerUrl());
            bannerVOS.add(bannerVO);
        }
        return bannerVOS;
    }

    private List<FilmInfo> getFilmInfos(List<MoocFilmT> moocFilms){
        List<FilmInfo> filmInfos = new ArrayList<>();
        for(MoocFilmT moocFilmT : moocFilms){
            FilmInfo filmInfo = new FilmInfo();
            filmInfo.setScore(moocFilmT.getFilmScore());
            filmInfo.setImgAddress(moocFilmT.getImgAddress());
            filmInfo.setFilmType(moocFilmT.getFilmType());
            filmInfo.setFilmScore(moocFilmT.getFilmScore());
            filmInfo.setFilmName(moocFilmT.getFilmName());
            filmInfo.setFilmId(moocFilmT.getUuid()+"");
            filmInfo.setExpectNum(moocFilmT.getFilmPresalenum());
            filmInfo.setBoxNum(moocFilmT.getFilmBoxOffice());
            filmInfo.setShowTime(DateUtil.getDay(moocFilmT.getFilmTime()));
            // 将转换的对象放入结果集
            filmInfos.add(filmInfo);
        }
        return filmInfos;
    }

    @Override
    public FilmVO getHotFilms(boolean isLimit, int nums,int nowPage,int sortId,int catId,int sourceId,int yearId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> filmInfos = new ArrayList<>();
        //热映影片的限制条件
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status",1);//影片状态,1-正在热映
        //判断是否是首页需要的内容
        if (isLimit){
            // 如果是，则限制条数、限制内容为热映影片
            Page<MoocFilmT> page = new Page<>(1,nums);
            List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
            //组织filmInfo
            filmInfos = getFilmInfos(moocFilmTS);
            filmVO.setFilmInfo(filmInfos);
            filmVO.setFilmNum(moocFilmTS.size());
        }else {
            //如果不是,则是列表页,同样需要限制内容为热映影片
            Page<MoocFilmT> page = null;
            // 根据sortId的不同，来组织不同的Page对象
            // 1-按热门搜索，2-按时间搜索，3-按评价搜索
            switch (sortId){
                case 1:
                    page = new Page<>(nowPage,nums,"film_box_office");
                    break;
                case 2:
                    page = new Page<>(nowPage,nums,"film_time");
                    break;
                case 3:
                    page = new Page<>(nowPage,nums,"film_score");
                    break;
                default:
                    page = new Page<>(nowPage,nums,"film_box_office");
                    break;
            }
            // 如果sourceId,yearId,catId 不为99 ,则表示要按照对应的编号进行查询
            if (sourceId!=99){
                entityWrapper.eq("film_source",sourceId);
            }
            if (yearId!=99){
                entityWrapper.eq("film_date",yearId);
            }
            if(catId!=99){
                String catStr = "%#" + catId + "#%";
                entityWrapper.like("film_cats",catStr);
            }
            List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
            //组织filmInfo
            filmInfos = getFilmInfos(moocFilmTS);
            filmVO.setFilmInfo(filmInfos);
            filmVO.setFilmNum(moocFilmTS.size());
            //总页数
            int totalCount = moocFilmTMapper.selectCount(entityWrapper);
            int totalpage = totalCount/nums + 1;
            filmVO.setNowPage(nowPage);
            filmVO.setTotalPage(totalpage);
        }
        return filmVO;
    }

    @Override
    public FilmVO getSoonFilms(boolean isLimit, int nums,int nowPage,int sortId,int catId,int sourceId,int yearId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> filmInfos = new ArrayList<>();
        //即将上映影片的限制条件
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status",2);//影片状态,2-即将上映
        //判断是否是首页需要的内容
        if (isLimit){
            // 如果是，则限制条数、限制内容为即将上映影片
            Page<MoocFilmT> page = new Page<>(1,nums);
            List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
            //组织filmInfo
            filmInfos = getFilmInfos(moocFilmTS);
            filmVO.setFilmInfo(filmInfos);
            filmVO.setFilmNum(moocFilmTS.size());
        }else {
            //如果不是,则是列表页,同样需要限制内容为即将上映影片
            Page<MoocFilmT> page = null;
            // 根据sortId的不同，来组织不同的Page对象
            // 1-按热门搜索，2-按时间搜索，3-按评价搜索
            switch (sortId){
                case 1:
                    page = new Page<>(nowPage,nums,"film_preSaleNum");
                    break;
                case 2:
                    page = new Page<>(nowPage,nums,"film_time");
                    break;
                case 3:
                    page = new Page<>(nowPage,nums,"film_preSaleNum");
                    break;
                default:
                    page = new Page<>(nowPage,nums,"film_preSaleNum");
                    break;
            }
            // 如果sourceId,yearId,catId 不为99 ,则表示要按照对应的编号进行查询
            if (sourceId!=99){
                entityWrapper.eq("film_source",sourceId);
            }
            if (yearId!=99){
                entityWrapper.eq("film_date",yearId);
            }
            if(catId!=99){
                String catStr = "%#" + catId + "#%";
                entityWrapper.like("film_cats",catStr);
            }
            List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
            //组织filmInfo
            filmInfos = getFilmInfos(moocFilmTS);
            filmVO.setFilmInfo(filmInfos);
            filmVO.setFilmNum(moocFilmTS.size());
            //总页数
            int totalCount = moocFilmTMapper.selectCount(entityWrapper);
            int totalpage = totalCount/nums + 1;
            filmVO.setNowPage(nowPage);
            filmVO.setTotalPage(totalpage);
        }
        return filmVO;
    }

    @Override
    public FilmVO getClassicFilms(int nums, int nowPage, int sortId, int catId, int sourceId, int yearId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> filmInfos = new ArrayList<>();
        //即将上映影片的限制条件
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status",3);//影片状态,2-即将上映,3-经典影片
        Page<MoocFilmT> page = null;
        // 根据sortId的不同，来组织不同的Page对象
        // 1-按热门搜索，2-按时间搜索，3-按评价搜索
        switch (sortId){
            case 1:
                page = new Page<>(nowPage,nums,"film_box_office");
                break;
            case 2:
                page = new Page<>(nowPage,nums,"film_time");
                break;
            case 3:
                page = new Page<>(nowPage,nums,"film_score");
                break;
            default:
                page = new Page<>(nowPage,nums,"film_box_office");
                break;
        }
        // 如果sourceId,yearId,catId 不为99 ,则表示要按照对应的编号进行查询
        if (sourceId!=99){
            entityWrapper.eq("film_source",sourceId);
        }
        if (yearId!=99){
            entityWrapper.eq("film_date",yearId);
        }
        if(catId!=99){
            String catStr = "%#" + catId + "#%";
            entityWrapper.like("film_cats",catStr);
        }
        List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
        //组织filmInfo
        filmInfos = getFilmInfos(moocFilmTS);
        filmVO.setFilmInfo(filmInfos);
        filmVO.setFilmNum(moocFilmTS.size());
        //总页数
        int totalCount = moocFilmTMapper.selectCount(entityWrapper);
        int totalpage = totalCount/nums + 1;
        filmVO.setNowPage(nowPage);
        filmVO.setTotalPage(totalpage);

        return filmVO;
    }

    @Override
    public List<FilmInfo> getBoxRanking() {
        // 条件 -> 正在上映的，票房前10名
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status",1);//影片状态,1-正在热映
        Page<MoocFilmT> page = new Page<>(1,10,"film_box_office");
        List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
        List<FilmInfo> filmInfos = getFilmInfos(moocFilmTS);
        return filmInfos;
    }

    @Override
    public List<FilmInfo> getExpectRanking() {
        // 条件 -> 即将上映的，预售前10名
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status",2);//影片状态,2-即将上映
        Page<MoocFilmT> page = new Page<>(1,10,"film_preSaleNum");
        List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
        List<FilmInfo> filmInfos = getFilmInfos(moocFilmTS);
        return filmInfos;
    }

    @Override
    public List<FilmInfo> getTop() {
        // 条件 -> 正在上映的，评分前10名
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status",1);//影片状态,1-正在热映
        Page<MoocFilmT> page = new Page<>(1,10,"film_score");
        List<MoocFilmT> moocFilmTS = moocFilmTMapper.selectPage(page, entityWrapper);
        List<FilmInfo> filmInfos = getFilmInfos(moocFilmTS);
        return filmInfos;
    }

    @Override
    public List<CatVO> getCatInfo() {
        List<CatVO> catVOList = new ArrayList<>();
        //查询实体对象
        List<MoocCatDictT> moocCatDictTS = moocCatDictTMapper.selectList(null);
        //将实体对象转换为业务对象
        for (MoocCatDictT moocCatDictT : moocCatDictTS){
            CatVO catVO = new CatVO();
            catVO.setCatId(moocCatDictT.getUuid()+"");
            catVO.setCatName(moocCatDictT.getShowName());
            catVOList.add(catVO);
        }
        return catVOList;
    }

    @Override
    public List<SourceVO> getSourceInfo() {
        List<SourceVO> sourceVOList = new ArrayList<>();
        //查询实体对象
        List<MoocSourceDictT> moocSourceDictTS = moocSourceDictTMapper.selectList(null);
        //将实体对象转换为业务对象
        for (MoocSourceDictT moocSourceDictT : moocSourceDictTS){
            SourceVO sourceVO = new SourceVO();
            sourceVO.setSourceId(moocSourceDictT.getUuid()+"");
            sourceVO.setSourceName(moocSourceDictT.getShowName());
            sourceVOList.add(sourceVO);
        }
        return sourceVOList;
    }

    @Override
    public List<YearVO> getYearInfo() {
        List<YearVO> yearVOList = new ArrayList<>();
        //查询实体对象
        List<MoocYearDictT> moocYearDictTS = moocYearDictTMapper.selectList(null);
        //将实体对象转换为业务对象
        for (MoocYearDictT moocYearDictT : moocYearDictTS){
            YearVO yearVO = new YearVO();
            yearVO.setYearId(moocYearDictT.getUuid()+"");
            yearVO.setYearName(moocYearDictT.getShowName());
            yearVOList.add(yearVO);
        }
        return yearVOList;
    }

    @Override
    public FilmDetailVO getFilmDetail(int searchType, String searchParam) {
        FilmDetailVO filmDetailVO = null;
        //searchType : ‘0表示按照编号查找，1表示按照名称查找’
        if (searchType == 0){
            filmDetailVO = moocFilmTMapper.getFilmDetailById(searchParam);
        }else {
            filmDetailVO = moocFilmTMapper.getFilmDetailByName("%" + searchParam + "%");
        }
        return filmDetailVO;
    }


    private MoocFilmInfoT getFilmInfo(String filmId){
        MoocFilmInfoT moocFilmInfoT = new MoocFilmInfoT();
        moocFilmInfoT.setFilmId(filmId);
        moocFilmInfoT = moocFilmInfoTMapper.selectOne(moocFilmInfoT);
        return moocFilmInfoT;
    }


    @Override
    public FilmDescVO getFilmDesc(String filmId) {
        MoocFilmInfoT moocFilmInfoT = getFilmInfo(filmId);
        FilmDescVO filmDescVO = new FilmDescVO();
        filmDescVO.setFilmId(filmId);
        filmDescVO.setBiography(moocFilmInfoT.getBiography());
        return filmDescVO;
    }

    @Override
    public ActorVO getDirect(String filmId) {
        MoocFilmInfoT moocFilmInfoT = getFilmInfo(filmId);
        //获取导演编号
        Integer directorId = moocFilmInfoT.getDirectorId();
        MoocActorT moocActorT = moocActorTMapper.selectById(directorId);
        ActorVO actorVO = new ActorVO();
        actorVO.setDirectorName(moocActorT.getActorName());
        actorVO.setImgAddress(moocActorT.getActorImg());
        return actorVO;
    }

    @Override
    public List<ActorVO> getActors(String filmId) {
        List<ActorVO> actors = moocActorTMapper.getActors(filmId);
        return actors;
    }

    @Override
    public ImgVO getImgs(String filmId) {
        MoocFilmInfoT moocFilmInfoT = getFilmInfo(filmId);
        //影片图片集地址,多个图片以逗号分隔
        String filmImgs = moocFilmInfoT.getFilmImgs();
        String[] imgs = filmImgs.split(",");
        ImgVO imgVO = new ImgVO();
        imgVO.setMainImg(imgs[0]);
        imgVO.setImg01(imgs[1]);
        imgVO.setImg02(imgs[2]);
        imgVO.setImg03(imgs[3]);
        imgVO.setImg04(imgs[4]);
        return imgVO;
    }
}
