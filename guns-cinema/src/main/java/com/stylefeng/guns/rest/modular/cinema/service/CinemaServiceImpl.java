package com.stylefeng.guns.rest.modular.cinema.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.*;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;

/**
 * @date 2019/6/28 17:06
 */
@Component
@Service(interfaceClass = CinemaServiceAPI.class)
public class CinemaServiceImpl implements CinemaServiceAPI {

    @Autowired
    private MoocCinemaTMapper moocCinemaTMapper;
    @Autowired
    private MoocBrandDictTMapper moocBrandDictTMapper;
    @Autowired
    private MoocAreaDictTMapper moocAreaDictTMapper;
    @Autowired
    private MoocHallDictTMapper moocHallDictTMapper;
    @Autowired
    private MoocFieldTMapper moocFieldTMapper;


    @Override
    public Page<CinemaVO> getCinemas(CinemaQueryVO cinemaQueryVO) {
        Page<MoocCinemaT> page = new Page<>(cinemaQueryVO.getNowPage(),cinemaQueryVO.getPageSize());
        //判断是否传入查询条件 --> brandId,districtId,hallType 是否等于99
        EntityWrapper<MoocCinemaT> entityWrapper = new EntityWrapper<>();
        if (cinemaQueryVO.getBrandId()!= 99){
            entityWrapper.eq("brand_id",cinemaQueryVO.getBrandId());
        }
        if (cinemaQueryVO.getDistrictId()!=99){
            entityWrapper.eq("area_id",cinemaQueryVO.getDistrictId());
        }
        if (cinemaQueryVO.getHallType()!=99){
            entityWrapper.like("hall_ids","%#" + cinemaQueryVO.getHallType() + "#%");
        }
        List<MoocCinemaT> moocCinemaTS = moocCinemaTMapper.selectPage(page, entityWrapper);
        List<CinemaVO> cinemaVOList = new ArrayList<>();
        //将数据实体转换为业务实体
        for (MoocCinemaT moocCinemaT : moocCinemaTS){
            CinemaVO cinemaVO = new CinemaVO();
            cinemaVO.setUuid(moocCinemaT.getUuid() + "");
            cinemaVO.setCinemaName(moocCinemaT.getCinemaName());
            cinemaVO.setAddress(moocCinemaT.getCinemaAddress());
            cinemaVO.setMinimumPrice(moocCinemaT.getMinimumPrice()+"");
            cinemaVOList.add(cinemaVO);
        }
        //查询影院总数
        Integer totalCount = moocCinemaTMapper.selectCount(entityWrapper);
        //组织返回对象
        Page<CinemaVO> result = new Page<>();
        result.setRecords(cinemaVOList);
        result.setTotal(totalCount);
        result.setSize(cinemaQueryVO.getPageSize());
        return result;
    }

    @Override
    public List<BrandVO> getBrands(int brandId) {
        List<BrandVO> brandVOList = new ArrayList<>();
        boolean flag = false;//标识位
        MoocBrandDictT moocBrandDictT = moocBrandDictTMapper.selectById(brandId);
        if (brandId == 99 || moocBrandDictT == null || moocBrandDictT.getUuid() == null){
            flag = true;
        }
        //查询所有列表
        List<MoocBrandDictT> moocBrandDictTS = moocBrandDictTMapper.selectList(null);
        for (MoocBrandDictT brandDictT : moocBrandDictTS){
            BrandVO brandVO = new BrandVO();
            brandVO.setBrandId(brandDictT.getUuid()+"");
            brandVO.setBrandName(brandDictT.getShowName());
            if (flag){
                if (brandDictT.getUuid()==99){
                    brandVO.setActive(true);
                }
            }else {
                if (brandDictT.getUuid()==brandId){
                    brandVO.setActive(true);
                }
            }
            brandVOList.add(brandVO);
        }
        return brandVOList;
    }

    @Override
    public List<AreaVO> getAreas(int areaId) {
        List<AreaVO> areaVOList = new ArrayList<>();
        boolean flag = false;//标识位
        MoocAreaDictT moocAreaDictT = moocAreaDictTMapper.selectById(areaId);
        if (areaId == 99 || moocAreaDictT == null || moocAreaDictT.getUuid() == null){
            flag = true;
        }
        //查询所有列表
        List<MoocAreaDictT> moocAreaDictTS = moocAreaDictTMapper.selectList(null);
        for (MoocAreaDictT areaDictT : moocAreaDictTS){
            AreaVO areaVO = new AreaVO();
            areaVO.setAreaId(areaDictT.getUuid()+"");
            areaVO.setAreaName(areaDictT.getShowName());
            if (flag){
                if (areaDictT.getUuid()==99){
                    areaVO.setActive(true);
                }
            }else {
                if (areaDictT.getUuid()==areaId){
                    areaVO.setActive(true);
                }
            }
            areaVOList.add(areaVO);
        }
        return areaVOList;
    }

    @Override
    public List<HallTypeVO> getHallTypes(int halltype) {
        List<HallTypeVO> hallTypeVOList = new ArrayList<>();
        boolean flag = false;//标识位
        MoocHallDictT moocHallDictT = moocHallDictTMapper.selectById(halltype);
        if (halltype == 99 || moocHallDictT == null || moocHallDictT.getUuid() == null){
            flag = true;
        }
        //查询所有列表
        List<MoocHallDictT> moocHallDictTS = moocHallDictTMapper.selectList(null);
        for (MoocHallDictT hallDictT : moocHallDictTS){
            HallTypeVO hallTypeVO = new HallTypeVO();
            hallTypeVO.setHalltypeId(hallDictT.getUuid()+"");
            hallTypeVO.setHalltypeName(hallDictT.getShowName());
            if (flag){
                if (hallDictT.getUuid()==99){
                    hallTypeVO.setActive(true);
                }
            }else {
                if (hallDictT.getUuid()==halltype){
                    hallTypeVO.setActive(true);
                }
            }
            hallTypeVOList.add(hallTypeVO);
        }
        return hallTypeVOList;
    }

    @Override
    public CinemaInfoVO getCinemaInfoById(int cinemaId) {
        //数据实体
        MoocCinemaT moocCinemaT = moocCinemaTMapper.selectById(cinemaId);
        //将数据实体转化为业务实体
        CinemaInfoVO cinemaInfoVO = new CinemaInfoVO();
        cinemaInfoVO.setCinemaId(moocCinemaT.getUuid()+"");
        cinemaInfoVO.setCinemaName(moocCinemaT.getCinemaName());
        cinemaInfoVO.setCinemaAdress(moocCinemaT.getCinemaAddress());
        cinemaInfoVO.setCinemaPhone(moocCinemaT.getCinemaPhone());
        cinemaInfoVO.setImgUrl(moocCinemaT.getImgAddress());
        return cinemaInfoVO;
    }

    @Override
    public List<FilmInfoVO> getFilmInfoByCinemaId(int cinemaId) {
        List<FilmInfoVO> filmInfoVOList = moocFieldTMapper.getFilmInfoByCinemaId(cinemaId);
        return filmInfoVOList;
    }

    @Override
    public HallInfoVO getFilmFieldInfo(int fieldId) {
        HallInfoVO hallInfo = moocFieldTMapper.getHallInfo(fieldId);
        return hallInfo;
    }

    @Override
    public FilmInfoVO getFilmInfoByFieldId(int fieldId) {
        FilmInfoVO filmInfo = moocFieldTMapper.getFilmInfoByField(fieldId);
        return filmInfo;
    }

    @Override
    public OrderQueryVO getOrderNeeds(int field) {
        OrderQueryVO orderQueryVO = new OrderQueryVO();
        MoocFieldT moocFieldT = moocFieldTMapper.selectById(field);
        orderQueryVO.setCinemaId(moocFieldT.getCinemaId()+"");
        orderQueryVO.setFilmId(moocFieldT.getFilmId()+"");
        orderQueryVO.setFilm_price(moocFieldT.getPrice()+"");
        return orderQueryVO;
    }
}
