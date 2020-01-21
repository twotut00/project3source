package com.stylefeng.guns.rest.modular.cinema;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.api.cinema.CinemaServiceApi;
import com.stylefeng.guns.api.cinema.vo.*;
import com.stylefeng.guns.api.order.OrderServiceApi;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: jia.xue
 * @create: 2019-06-15 15:19
 * @Description
 **/
@RestController
@RequestMapping(value = "/cinema")
public class CinemaController {

    private static final String IMG_PRE = "http://img.meetingshop.cn/";
    private static final String TRUE = "TRUE";
    private static final String FALSE = "FALSE";

    private transient static final Logger logger = LoggerFactory.getLogger(CinemaController.class);
    @Reference(interfaceClass = CinemaServiceApi.class,check = false)
    private CinemaServiceApi cinemaService;
    @Reference(interfaceClass = OrderServiceApi.class,check = false)
    private OrderServiceApi orderServiceApi;
    /**
     * 1、查询影院列表-根据条件查询所有影院
     * @return
     */
    @RequestMapping(value = "/getCinemas",method = RequestMethod.GET)
    public ResponseVO getCinemas(CinemaRequestVO cinemaRequestVO){
        CinemaResponseVO cinemaResponseVO;
        try{
            cinemaResponseVO = cinemaService.getCinemaList(cinemaRequestVO);
            return ResponseVO.success(cinemaResponseVO.getData(),cinemaResponseVO.getNowPage(),cinemaResponseVO.getTotalPage(),IMG_PRE);
        }catch (Exception e) {
            logger.info("根据cinemaRequestVO--[{}] 查询所有影院异常！",JSON.toJSONString(cinemaRequestVO));
            e.printStackTrace();
            return ResponseVO.exception("影院信息查询失败");
        }

    }

    /**
     * 2、获取影院列表查询条件
     * @return
     */
    @RequestMapping(value = "/getCondition",method = RequestMethod.GET)
    public ResponseVO getCondition(@RequestParam(defaultValue = "99",required = false,name =  "brandId") Integer brandId,
                                   @RequestParam(defaultValue = "99",required = false,name =  "hallType") Integer hallType,
                                   @RequestParam(defaultValue = "1",required = false,name =  "areaId") Integer areaId){
        try {
            List<AreaVO> areaList = cinemaService.getAreaList(areaId);
            List<BrandVO> brandList = cinemaService.getBrandList(brandId);
            List<HallTypeVO> halltypeList = cinemaService.getHalltypeList(hallType);

            CinemaConditionVO cinemaConditionVO = new CinemaConditionVO();
            cinemaConditionVO.setAreaList(areaList);
            cinemaConditionVO.setBrandList(brandList);
            cinemaConditionVO.setHalltypeList(halltypeList);
            return ResponseVO.success(cinemaConditionVO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("获取影院查询条件异常，brandId:{}, hallType:{}, areaId:{}",brandId,hallType,areaId);
            return ResponseVO.fail("影院信息查询失败");
        }
    }

    /**
     * 3、获取播放场次接口
     * @param cinemaId
     * @return
     */
    @RequestMapping(value = "/getFields")
    public ResponseVO getFields(Integer cinemaId){
        try {
            CinemaInfoVO cinemaInfo = cinemaService.getCinemaInfo(cinemaId);
            List<FilmVO> films = cinemaService.getFilmsByCinemaId(cinemaId);
            FieldsVo fieldsVo = new FieldsVo();
            fieldsVo.setCinemaInfo(cinemaInfo);
            fieldsVo.setFilmList(films);
            return ResponseVO.success(IMG_PRE,fieldsVo);
        } catch (Exception e) {
            logger.info("影院信息查询失败, cinemaId:{}",cinemaId);
            e.printStackTrace();
            return ResponseVO.fail("播放场次信息查询失败");
        }
    }

    /**
     * 4、获取场次详细信息接口
     * @param cinemaId
     * @return
     */
    @RequestMapping(value = "/getFieldInfo", method = RequestMethod.POST)
    public ResponseVO getFieldInfo(Integer cinemaId,Integer fieldId,@RequestParam(required = false,defaultValue = FALSE)String isNeedDiscountPrice){
        try {
            Boolean flag = false;
            if (TRUE.equalsIgnoreCase(isNeedDiscountPrice)) {
                flag = true;
            }
            FieldDetailInfoVO fieldDetail = cinemaService.getFieldDetail(cinemaId, fieldId,flag);
            HallInfoVO hallInfo = fieldDetail.getHallInfo();
            String soldSeats = orderServiceApi.getSoldSeatsByFieldId(fieldId);
            hallInfo.setSoldSeats(soldSeats);
            fieldDetail.setHallInfo(hallInfo);

            return ResponseVO.success(IMG_PRE,fieldDetail);
        } catch (Exception e) {
            logger.info("获取场次详细信息接口异常！cinemaId:{},fieldId:{}",cinemaId,fieldId);
            e.printStackTrace();
            return ResponseVO.fail("场次详细信息接口查询失败");
        }
    }



}