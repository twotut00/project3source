package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.api.promo.vo.PromoVO;
import com.stylefeng.guns.rest.common.persistence.model.MtimePromo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ciggar
 * @since 2019-08-08
 */
public interface MtimePromoMapper extends BaseMapper<MtimePromo> {

    List<PromoVO> queryPromosByCinemaId(@Param(value = "cinemaId") Integer cinemaId,@Param(value = "promoStatus")Integer status);
}
