package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.api.cinema.vo.FilmVO;
import com.stylefeng.guns.api.cinema.vo.HallInfoVO;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFieldT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 放映场次表 Mapper 接口
 * </p>
 *
 * @author ciggar
 * @since 2019-06-15
 */
public interface MtimeFieldTMapper extends BaseMapper<MtimeFieldT> {

    List<FilmVO> getFilms(@Param(value = "cinemaId") Integer cinemaId);

    HallInfoVO getFields(@Param(value = "fieldId") Integer fieldId);

    FilmVO getFilmInfoByFieldId(@Param(value = "fieldId")Integer fieldId);

}
