package com.stylefeng.guns.api.cinema;

import com.stylefeng.guns.api.cinema.vo.*;

import java.util.List;

/**
 * @author: jia.xue
 * @create: 2019-06-14 18:10
 * @Description
 **/
public interface CinemaServiceApi {

    /**
     * 按条件 分页查询影院列表
     * @param cinemaRequestVO
     * @return
     */
    CinemaResponseVO getCinemaList(CinemaRequestVO cinemaRequestVO);

    /**
     * 根据id查询影院品牌
     * @param brandId
     * @return
     */
    List<BrandVO> getBrandList(Integer brandId);

    /**
     * 根据地区id查询地区
     * @param areaId
     * @return
     */
    List<AreaVO> getAreaList(Integer areaId);

    /**
     * 根据影厅类型id查询影厅
     * @param hallTypeId
     * @return
     */
    List<HallTypeVO> getHalltypeList(Integer hallTypeId);

    /**
     * 根据影院的id查询影院信息
     * @param cinemaId
     * @return
     */
    CinemaInfoVO getCinemaInfo(Integer cinemaId);


    /**
     * 根据影院id获取该影院放映的电影list
     * @param cinemaId
     * @return
     */
    List<FilmVO> getFilmsByCinemaId(Integer cinemaId);

    /**
     * 根据影院id 和场次id 获取场次详细信息
     * @param cinemaId
     * @param fieldId
     * @return
     */
    FieldDetailInfoVO getFieldDetail(Integer cinemaId, Integer fieldId, Boolean isNeedDiscountPrice);


    FieldOrderVO getFieldById(Integer fieldId);
}