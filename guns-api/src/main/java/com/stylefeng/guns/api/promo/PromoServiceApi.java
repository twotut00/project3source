package com.stylefeng.guns.api.promo;


import com.stylefeng.guns.api.promo.vo.PromoOrderVO;
import com.stylefeng.guns.api.promo.vo.PromoVO;

import java.util.List;

/**
 * @author: jia.xue
 * @create: 2019-08-07 12:05
 * @Description 秒杀活动service
 **/
public interface PromoServiceApi {

    /**
     * 通过影院id查询正在进行中的秒杀活动
     * 如果cinemaId为空，就查全部正在进行中的秒杀活动
     * @param cinemaId
     * @return
     */
    List<PromoVO> getPromoByCinemaId(Integer cinemaId);

    /**
     * 生成秒杀订单
     * 扣减库存
     * 扣钱
     * @param promoId
     * @param userId
     * @param amount
     * @return PromoOrderVO
     */
    PromoOrderVO savePromoOrderVO(Integer promoId,Integer userId,Integer amount,String stockLogId) throws Exception;


    /**
     * 事务型生成秒杀订单
     * 扣减库存
     * 扣钱
     * @param promoId
     * @param userId
     * @param amount
     * @return PromoOrderVO
     */
    Boolean transactionSavePromoOrderVO(Integer promoId,Integer userId,Integer amount,String stockLogId) throws Exception;

    /**
     * 扣减库存
     * @param promoId
     * @param amount
     * @return
     */
    Boolean decreaseStock(Integer promoId,Integer amount);


    /**
     * 异步更新库存
     * @param promoId
     * @param amount
     * @return
     */
    Boolean asyncDecreaseStock(Integer promoId,Integer amount);

    /**
     * 回补库存
     * @param promoId
     * @param amount
     * @return
     */
    Boolean increaseock(Integer promoId,Integer amount);



    /**
     * 将对应的cinemaId发布到缓存中，如果cinemaId为空，那么就将所有的库存信息发布到缓存中
     * @param cinemaId
     * @return
     */
    Boolean publishPromoStock(Integer cinemaId);

    /**
     * 初始化库存流水
     * @param promoId
     * @param amount
     * @return
     */
    String initStockLog(Integer promoId,Integer amount);

    /**
     * 生成秒杀令牌接口
     * @param promoId
     * @param userId
     * @return
     */
    String generatePromoToken(Integer promoId,Integer userId);

    /**
     * 根据promoId获取活动信息
     * @param promoId
     * @return
     */
    PromoVO getPromoById(Integer promoId);
}