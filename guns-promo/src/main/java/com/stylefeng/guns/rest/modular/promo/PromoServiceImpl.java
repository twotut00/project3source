package com.stylefeng.guns.rest.modular.promo;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.api.promo.PromoServiceApi;
import com.stylefeng.guns.api.promo.vo.PromoOrderVO;
import com.stylefeng.guns.api.promo.vo.PromoVO;
import com.stylefeng.guns.api.user.UserApi;
import com.stylefeng.guns.api.util.CodeCreator;
import com.stylefeng.guns.api.util.CodeCreatorPreConstants;
import com.stylefeng.guns.core.constant.PromoStatus;
import com.stylefeng.guns.core.constant.StockLogStatus;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.exception.GunsExceptionEnum;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoOrderMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoStockMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeStockLogMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimePromo;
import com.stylefeng.guns.rest.common.persistence.model.MtimePromoOrder;
import com.stylefeng.guns.rest.common.persistence.model.MtimeStockLog;
import com.stylefeng.guns.rest.modular.mq.Mqproducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author: jia.xue
 * @create: 2019-08-07 12:08
 * @Description 秒杀活动serviceImpl
 **/
@Slf4j
@Component
@Service(interfaceClass = PromoServiceApi.class)
public class PromoServiceImpl implements PromoServiceApi {

    @Autowired
    private MtimePromoMapper promoMapper;

    @Autowired
    private MtimePromoOrderMapper promoOrderMapper;

    @Autowired
    private MtimePromoStockMapper promoStockMapper;

    @Autowired
    private MtimeStockLogMapper mtimeStockLogMapper;

    @Autowired
    private CodeCreator codeCreator;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private Mqproducer mqproducer;

    @Reference(interfaceClass = UserApi.class,check = false)
    private UserApi userService;

    //兑换码有效时间设置为3个月
    private static final Integer VALID_MONTH = 3;

    //秒杀活动缓存key前缀
    private static final String PROMO_CACHE_PREFIX = "promo_cache_prefix_id_";

    //秒杀活动缓存时间设置为3天
    private static final Integer PROMO_CACHE_TIME_DAYS = 3;

    //库存售罄标识
    private static final String PROMO_STOCK_INVALID_PROMOID_ = "promo_stock_invalid_promoId_";

    //秒杀令牌过期时间设置为5分钟
    private static final Integer TOKEN_CACHE_TIME = 3;

    //秒杀令牌缓存key
    private static final String PROMO_TOKEN_KEY_USERID_ = "promo_token_key_userid_promoid_";

    //秒杀令牌限制数量key
    private static final String PROMO_TOKEN_LIMIT_COUNT_KEY = "promo_token_limit_count_key_";
    @Override
    public List<PromoVO> getPromoByCinemaId(Integer cinemaId) {
        List<PromoVO> promoVOS = promoMapper.queryPromosByCinemaId(cinemaId,PromoStatus.ING.getIndex());
        return promoVOS;
    }

    @Override
    public Boolean transactionSavePromoOrderVO(Integer promoId, Integer userId, Integer amount,String stockLogId) throws Exception {
        Boolean ret = mqproducer.transactionAsyncReduceStock(promoId, userId, amount,stockLogId);
        return ret;
    }

    /**
     * 生成秒杀订单
     * 扣减库存
     * 扣钱
     * @param promoId
     * @param userId
     * @param amount
     * @return
     */
    @Transactional
    @Override
    public PromoOrderVO savePromoOrderVO(Integer promoId, Integer userId, Integer amount,String stockLogId) throws Exception{

        //参数校验
        processParam(promoId,userId,amount);

        MtimePromo promo = promoMapper.selectById(promoId);

        String key = PROMO_CACHE_PREFIX+promoId;

        //扣减redis库存
        Boolean operateStock = this.decreaseStock(promoId, amount);
        if (!operateStock) {
            log.info("扣减库存失败！promoId:{},amount:{}",promo.getUuid(),amount);
            throw new GunsException(GunsExceptionEnum.STOCK_ERROR);
        }

        //订单入库
        MtimePromoOrder promoOrder = savePromoOrder(promo, userId, amount);
        if (promoOrder == null) {
            this.increaseock(promoId,amount);
            throw new GunsException(GunsExceptionEnum.DATABASE_ERROR);
        }

        PromoOrderVO promoOrderVO =  buildPromoOrderVO(promoOrder);

//        //异步更新库存
//        Boolean ret = this.asyncDecreaseStock(promoId, amount);
//        if (!ret) {
//            log.info("异步更新库存失败！promoId:{}, amount:{}",promoId,amount);
//            this.increaseock(promoId,amount);
//            throw new GunsException(GunsExceptionEnum.STOCK_ERROR);
//        }

        //更新库存流水状态
        MtimeStockLog stockLog = mtimeStockLogMapper.selectById(stockLogId);
        stockLog.setStatus(StockLogStatus.SUCCESS.getIndex());
        Integer affectedRows = mtimeStockLogMapper.updateById(stockLog);
        if (affectedRows < 1) {
            log.info("更新库存流水状态失败,stockLog:{}",JSON.toJSONString(stockLog));
            throw new GunsException(GunsExceptionEnum.STOCK_ERROR);
        }
        //返回前端
        return promoOrderVO;
    }

    //参数校验
    private void processParam(Integer promoId, Integer userId, Integer amount) {
        if (promoId == null) {
            log.info("promoId不能为空！");
            throw new GunsException(GunsExceptionEnum.REQUEST_NULL);
        }
        if (userId == null) {
            log.info("userId不能为空！");
            throw new GunsException(GunsExceptionEnum.REQUEST_NULL);
        }
        if (amount == null) {
            log.info("amount不能为空！");
            throw new GunsException(GunsExceptionEnum.REQUEST_NULL);
        }
    }


    //生成秒杀订单
    MtimePromoOrder savePromoOrder(MtimePromo promo,Integer userId, Integer amount){
        MtimePromoOrder promoOrder = buidPromoOrder(promo,userId,amount);
        Integer insertRet = promoOrderMapper.insert(promoOrder);
        if (insertRet < 1) {
            log.info("生成秒杀订单失败！promoOrder:{}",JSON.toJSONString(promoOrder));
            return null;
        }
        return promoOrder;
    }



    /**
     * 1.标志REQUIRES_NEW会新开启事务，外层事务不会影响内部事务的提交/回滚
     * 2.标志REQUIRES_NEW的内部事务的异常，会影响外部事务的回滚
     * 事务的传播机制 Propagation.REQUIRES_NEW
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Boolean decreaseStock(Integer promoId, Integer amount) {

        String key = PROMO_CACHE_PREFIX+promoId;
        Long newAmount = redisTemplate.opsForValue().increment(key, amount * -1);
        if (newAmount < 0) {
            log.info("库存扣减失败！promoId:{},amount:{}",promoId,amount);
            redisTemplate.opsForValue().increment(key, amount);
            return false;
        }
        if (newAmount == 0) {
            //如果库存为0,那么打上库存售罄的标识
            redisTemplate.opsForValue().set(PROMO_STOCK_INVALID_PROMOID_+promoId,"true");
        }
        log.info("从缓存扣减库存成功！promoId:{},amount:{}",promoId,amount);
        return true;
    }






    private PromoOrderVO buildPromoOrderVO(MtimePromoOrder promoOrder) {
        PromoOrderVO orderVO = new PromoOrderVO();
        orderVO.setUuid(promoOrder.getUuid());
        orderVO.setUserId(promoOrder.getUserId());
        orderVO.setAffectedEndTime(promoOrder.getEndTime());
        orderVO.setAffectedStartTime(promoOrder.getStartTime());
        orderVO.setAmount(promoOrder.getAmount());
        orderVO.setCinemaId(promoOrder.getCinemaId());
        orderVO.setCreateTime(promoOrder.getCreateTime());
        orderVO.setExchangeCode(promoOrder.getExchangeCode());
        orderVO.setPrice(promoOrder.getPrice().doubleValue());
        return orderVO;
    }

    private MtimePromoOrder buidPromoOrder(MtimePromo promo, Integer userId, Integer amount) {
        MtimePromoOrder promoOrder = new MtimePromoOrder();
        String uuid = codeCreator.createNo(CodeCreatorPreConstants.PROMO_ORDER_NO.getPRE());
        Integer cinemaId = promo.getCinemaId();
        String exchangeCode = codeCreator.createNo(CodeCreatorPreConstants.EXCHANGE_CODE_NO.getPRE());
        //兑换开始时间和兑换结束时间 为从现在开始，到未来三个月之内
        Date startTime = new Date();
        Date endTime = DateUtil.getAfterMonthDate(VALID_MONTH);
        BigDecimal amountDecimal = new BigDecimal(amount);
        BigDecimal price = amountDecimal.multiply(promo.getPrice()).setScale(2,RoundingMode.HALF_UP);
        promoOrder.setUuid(uuid);
        promoOrder.setUserId(userId);
        promoOrder.setCinemaId(cinemaId);
        promoOrder.setExchangeCode(exchangeCode);
        promoOrder.setStartTime(startTime);
        promoOrder.setEndTime(endTime);
        promoOrder.setAmount(amount);
        promoOrder.setPrice(price);
        promoOrder.setCreateTime(new Date());
        return  promoOrder;
    }


    @Override
    public Boolean publishPromoStock(Integer cinemaId) {
        List<PromoVO> promoVOS = this.getPromoByCinemaId(cinemaId);
        if (CollectionUtils.isEmpty(promoVOS)) {
            log.info("查询秒杀活动信息为空，存入缓存失败！cinemaId:{}",cinemaId);
            return Boolean.FALSE;
        }
        for (PromoVO promoVO : promoVOS) {
            Integer uuid = promoVO.getUuid();
            Integer stock = promoVO.getStock();
            String key = PROMO_CACHE_PREFIX+uuid;
            //将活动发布信息存入缓存中并设置过期时间为3天
            redisTemplate.opsForValue().set(key,String.valueOf(stock));
            redisTemplate.expire(key,PROMO_CACHE_TIME_DAYS,TimeUnit.DAYS);

            //将秒杀令牌的发放数量限制存入redis中
            redisTemplate.opsForValue().set(PROMO_TOKEN_LIMIT_COUNT_KEY+uuid,(stock * 5) + "");
        }
        return Boolean.TRUE;
    }


    @Override
    public Boolean increaseock(Integer promoId, Integer amount) {
        String key = PROMO_CACHE_PREFIX + promoId;
        redisTemplate.opsForValue().increment(key,amount);
        return true;
    }

    @Override
    public Boolean asyncDecreaseStock(Integer promoId, Integer amount) {
        Boolean mqResult = mqproducer.asyncReduceStock(promoId, amount);
        return mqResult;
    }

    @Override
    public String initStockLog(Integer promoId, Integer amount) {
        MtimeStockLog stockLog = new MtimeStockLog();
        stockLog.setPromoId(promoId);
        stockLog.setAmount(amount);
        stockLog.setStatus(StockLogStatus.INIT.getIndex());
        String logNo = codeCreator.createNo(CodeCreatorPreConstants.STOCK_LOG_NO.getPRE());
        stockLog.setUuid(logNo);
        Integer affectedRows = mtimeStockLogMapper.insert(stockLog);
        if (affectedRows >0) {
            log.info("初始化库存流水成功！stockLog:{}",JSON.toJSONString(stockLog));
            return logNo;
        }
        return null;
    }

    @Override
    public String generatePromoToken(Integer promoId, Integer userId) {
        if (promoId == null || userId == null) {
            log.info("传入参数不合法！promoId:{},userId:{}",promoId,userId);
            return  null;
        }
//        MtimePromo mtimePromo = promoMapper.selectById(promoId);
//        if(mtimePromo == null) {
//            log.info("没有根据条件查到活动信息，promoId:{},userId:{}",promoId,userId);
//            return null;
//        }
        String promoToken = UUID.randomUUID().toString().replace("-", "");
        String key = PROMO_TOKEN_KEY_USERID_+userId+promoId;

        redisTemplate.opsForValue().set(key,promoToken);
        redisTemplate.expire(key,TOKEN_CACHE_TIME,TimeUnit.MINUTES);
        return promoToken;
    }

    @Override
    public PromoVO getPromoById(Integer promoId) {
        MtimePromo mtimePromo = promoMapper.selectById(promoId);
        if (mtimePromo == null) {
            return null;
        }
        PromoVO promoVO = new PromoVO();
        BeanUtils.copyProperties(mtimePromo,promoVO);
        return promoVO;
    }
}