package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimePromoStock;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ciggar
 * @since 2019-08-08
 */
public interface MtimePromoStockMapper extends BaseMapper<MtimePromoStock> {

    Integer decreaseStock(@Param(value = "promoId") Integer promoId,
                          @Param(value = "amount") Integer amount);
}
