package com.stylefeng.guns.rest.modular.cinema;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.*;
import com.stylefeng.guns.rest.modular.cinema.vo.CinemaConditionResponseVO;
import com.stylefeng.guns.rest.modular.cinema.vo.CinemaFieldResponseVO;
import com.stylefeng.guns.rest.modular.cinema.vo.CinemaFieldsResponseVO;
import com.stylefeng.guns.rest.modular.cinema.vo.CinemaListResposeVO;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @date 2019/6/27 14:19
 */
@Slf4j
@RestController
@RequestMapping("/cinema")
public class CinemaController {

    /**
     *  结果缓存,用于加速热门数据的访问速度，Dubbo 提供声明式缓存，以减少用户加缓存的工作量
     *  缓存类型:
     *  lru 基于最近最少使用原则删除多余缓存，保持最热的数据被缓存。
     *  threadlocal 当前线程缓存，比如一个页面渲染，用到很多 portal，每个 portal 都要去查用户信息，通过线程缓存，可以减少这种多余访问。
     *  jcache 与 JSR107 集成，可以桥接各种缓存实现。
     */
    @Reference(interfaceClass = CinemaServiceAPI.class,cache = "lru",check = false)
    private CinemaServiceAPI cinemaServiceAPI;
    /**
     * 查询影院列表-根据条件查询所有影院
     * @param cinemaQueryVO
     * @return
     */
    @GetMapping("/getCinemas")
    public ResponseVO getCinemas(CinemaQueryVO cinemaQueryVO){
        try {
            //根据五个条件进行筛选
            Page<CinemaVO> cinemas = cinemaServiceAPI.getCinemas(cinemaQueryVO);
            //判断是否有满足条件的数据
            if (CollectionUtils.isEmpty(cinemas.getRecords())){
                return ResponseVO.success("无影院可查");
            }else {
                CinemaListResposeVO cinemaListResposeVO = new CinemaListResposeVO();
                cinemaListResposeVO.setCinemas(cinemas.getRecords());
                return ResponseVO.success(cinemaListResposeVO,cinemaQueryVO.getNowPage(),
                        (int) cinemas.getPages());
            }
        }catch (Exception e){
            log.error("获取影院列表异常",e);
            return ResponseVO.serviceFail("查询影院列表失败");
        }
    }

    /**
     * 获取影院列表查询条件
     * @param cinemaQueryVO
     * @return
     */
    @GetMapping("/getCondition")
    public ResponseVO getCondition(CinemaQueryVO cinemaQueryVO){
        try{
            //获取三个条件集合,封装成一个对象返回
            List<BrandVO> brandList = cinemaServiceAPI.getBrands(cinemaQueryVO.getBrandId());
            List<AreaVO> areaList = cinemaServiceAPI.getAreas(cinemaQueryVO.getDistrictId());
            List<HallTypeVO> halltypeList = cinemaServiceAPI.getHallTypes(cinemaQueryVO.getHallType());
            CinemaConditionResponseVO cinemaConditionVO = new CinemaConditionResponseVO();
            cinemaConditionVO.setBrandList(brandList);
            cinemaConditionVO.setAreaList(areaList);
            cinemaConditionVO.setHalltypeList(halltypeList);
            return ResponseVO.success(cinemaConditionVO);
        }catch (Exception e){
            log.error("获取影院查询条件列表失败",e);
            return ResponseVO.serviceFail("获取影院查询条件列表失败");
        }
    }


    /**
     * 	获取播放场次接口
     * @param cinemaId
     * @return
     */
    @PostMapping("/getFields")
    public ResponseVO getFields(Integer cinemaId){
        try{
            CinemaInfoVO cinemaInfo = cinemaServiceAPI.getCinemaInfoById(cinemaId);
            List<FilmInfoVO> filmList = cinemaServiceAPI.getFilmInfoByCinemaId(cinemaId);
            CinemaFieldsResponseVO cinemaFieldResponseVO = new CinemaFieldsResponseVO();
            cinemaFieldResponseVO.setCinemaInfo(cinemaInfo);
            cinemaFieldResponseVO.setFilmList(filmList);
            return ResponseVO.success(cinemaFieldResponseVO,"http://img.meetingshop.cn/");
        }catch (Exception e){
            log.error("获取播放场次接口失败",e);
            return ResponseVO.serviceFail("获取播放场次接口失败");
        }
    }

    /**
     * 获取场次详细信息接口
     * @param cinemaId
     * @param fieldId
     * @return
     */
    @PostMapping("/getFieldInfo")
    public ResponseVO getFieldInfo(Integer cinemaId,Integer fieldId){
        try{
            CinemaInfoVO cinemaInfoVO = cinemaServiceAPI.getCinemaInfoById(cinemaId);
            FilmInfoVO filmInfo = cinemaServiceAPI.getFilmInfoByFieldId(fieldId);
            HallInfoVO hallInfoVO = cinemaServiceAPI.getFilmFieldInfo(fieldId);
            //TODO 后续会对接订单接口
            hallInfoVO.setSoldSeats("1,2,3");

            CinemaFieldResponseVO cinemaFieldResponseVO = new CinemaFieldResponseVO();
            cinemaFieldResponseVO.setCinemaInfo(cinemaInfoVO);
            cinemaFieldResponseVO.setFilmInfo(filmInfo);
            cinemaFieldResponseVO.setHallInfo(hallInfoVO);
            return ResponseVO.success(cinemaFieldResponseVO,"http://img.meetingshop.cn/");

        }catch (Exception e){
            log.error("获取场次详细信息接口失败",e);
            return ResponseVO.serviceFail("获取场次详细信息接口失败");
        }
    }
}
