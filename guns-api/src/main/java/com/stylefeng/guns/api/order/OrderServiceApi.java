package com.stylefeng.guns.api.order;

import com.stylefeng.guns.api.order.vo.OrderVO;

import java.util.List;

public interface OrderServiceApi {

    //购票

    /**
     * 1，验证售出的票是否为真
     * @param seats
     * @param fieldId
     * @return 如果该座位存在，返回true 如果该座位不存在，返回false
     */
    Boolean isTrueSeats(String seats,String fieldId);

    /**
     * 查已经销售的订单 是否有这个座位
     * @param fieldId
     * @param seats
     * @return 如果已经售出该座位，返回false，如果没有，返回true
     */
    Boolean isNotSoldSeats(String fieldId,String seats);

    /**创建订单信息
     *
     * @param fieldId
     * @param soldSeats
     * @param seatsName
     * @param userId
     * @return com.stylefeng.guns.api.order.vo.OrderVO
     */
    OrderVO saveOrderInfo(Integer fieldId, String soldSeats, String seatsName,Integer userId,Integer discountPrice);
    //获取订单信息


    /**
     * 2，获取当前登录人已经购买的订单
     * @param userId
     * @return
     */
    List<OrderVO> getOrdersByUserId(Integer userId,Integer nowPage,Integer pageSize);



    /**
     * 根据fieldId获取所有已经销售的座位编号
     */

    String getSoldSeatsByFieldId(Integer fieldId);

    /**
     * 根据订单id获取订单信息
     * @param orderId
     * @return
     */
    OrderVO getOrderVOById(String orderId);

    /**
     * 修改状态为支付成功
     * @param orderId
     * @return
     */
    Boolean paySuccess(String orderId);

    /**
     * 修改状态为支付失败
     * @param orderId
     * @return
     */
    Boolean payFail(String orderId);

//    /**
//     * 获取所有的秒杀活动列表
//     * @return
//     */
//    List<PromoVO> getAllPromo(Integer promoStatus);
//
//    PromoVO getPromoByFieldId(Integer fieldId);
//
//    OrderVO savePromoOrder(Integer fieldId, String soldSeats, String seatsName, Integer integer, Integer discountPrice, Integer promoId);
}
