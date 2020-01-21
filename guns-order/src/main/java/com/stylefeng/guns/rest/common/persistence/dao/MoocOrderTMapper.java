package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.api.order.vo.OrderVO;
import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 订单信息表 Mapper 接口
 * </p>
 *
 * @author ciggar
 * @since 2019-06-16
 */
public interface MoocOrderTMapper extends BaseMapper<MoocOrderT> {

    String getSeatsByFieldId(@Param(value = "fieldId") Integer fieldId);

    MoocOrderT getOrderDetail(@Param(value = "fielId") Integer fieldId);

    List<OrderVO> getOrderListByUserId(@Param(value = "userId") Integer userId,
                                       @Param(value = "startIndex")Integer startIndex,
                                       @Param(value = "pageSize")Integer pageSize);

    OrderVO getOrderVOById(@Param(value = "orderId") String orderId);
}
