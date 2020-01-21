package com.stylefeng.guns.rest.modular.promo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.RateLimiter;
import com.stylefeng.guns.api.order.OrderServiceApi;
import com.stylefeng.guns.api.promo.PromoServiceApi;
import com.stylefeng.guns.api.promo.vo.PromoOrderVO;
import com.stylefeng.guns.api.promo.vo.PromoVO;
import com.stylefeng.guns.core.constant.PromoStatus;
import com.stylefeng.guns.core.constant.ResponseStatus;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.exception.GunsExceptionEnum;
import com.stylefeng.guns.rest.modular.auth.util.TokenUtil;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.*;


/**
 * @author: jia.xue
 * @create: 2019-08-05 17:13
 * @Description
 **/
@Slf4j
@RestController
@RequestMapping("/promo")
public class PromoController {

    @Reference(interfaceClass = PromoServiceApi.class,check = false)
    private PromoServiceApi promoServiceApi;

    @Reference(interfaceClass = OrderServiceApi.class,check = false)
    private OrderServiceApi orderService;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    //库存售罄标识
    private static final String PROMO_STOCK_INVALID_PROMOID_ = "promo_stock_invalid_promoId_";
    //promoToken 缓存key前缀
    private static final String PROMO_TOKEN_KEY_USERID_ = "promo_token_key_userid_promoid_";

    //秒杀令牌限制数量key
    private static final String PROMO_TOKEN_LIMIT_COUNT_KEY = "promo_token_limit_count_key_";

    private ExecutorService executorService;

    private RateLimiter orderCreateRateLimiter;

    @PostConstruct
    public void init(){
        //拥塞窗口大小 20
        executorService = Executors.newFixedThreadPool(20);

        //限流大小200
        orderCreateRateLimiter = RateLimiter.create(200);
    }

    /**
     * 根据影院id查询秒杀订单列表
     * @param cinemaId
     * @return
     */
    @RequestMapping(value = "/getPromo",method = RequestMethod.GET)
    @ResponseBody
    public ResponseVO getPromo(@RequestParam(required = false, name = "cinemaId") Integer cinemaId){
        List<PromoVO> promoVOS = promoServiceApi.getPromoByCinemaId(cinemaId);
        return ResponseVO.success(promoVOS);
    }


    /**
     * 秒杀订单下单接口
     *
     * @param promoId 秒杀id
     * @param amount 下单购买数量
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/createOrder",method = RequestMethod.POST)
    @ResponseBody
    public ResponseVO savePromoOrder(@RequestParam(required = true, name = "promoId") Integer promoId,
                                       @RequestParam(required = true, name = "amount") Integer amount,
                                     @RequestParam(required = true, name = "promoToken") String promoToken,
                                       HttpServletRequest request, HttpServletResponse response){
        if (orderCreateRateLimiter.acquire() < 0) {
            return ResponseVO.fail("活动太火爆，请稍后再试！");
        }

        //参数判断
        ResponseVO responseVO = tokenUtil.getUserId(request, response);
        String userId = (String) responseVO.getMsg();
        if (responseVO == null || responseVO.getStatus() != ResponseStatus.success.getIndex() || StringUtils.isEmpty(userId)) {
            log.info("获取用户失败！请用户重新登录！responseVO:{}",JSON.toJSONString(responseVO));
            return ResponseVO.expire();
        }
//        PromoOrderVO promoOrderVO = promoServiceApi.savePromoOrderVO(promoId, Integer.valueOf(userId), amount);
//        if (promoOrderVO == null) {
//            log.info("创建秒杀订单失败！promoId:{},userId:{},amount:{},promoOrderVO:{}",promoId,userId,amount,promoOrderVO);
//            return ResponseVO.fail("创建秒杀订单失败!");
//        }
        if (amount < 0 || amount > 10) {
            return ResponseVO.fail("订单数量不合法！");
        }
        if (StringUtils.isBlank(promoToken)) {
            return ResponseVO.fail("参数不合法！");
        }
        String promoTokenInRedis = redisTemplate.opsForValue().get(PROMO_TOKEN_KEY_USERID_ + userId + promoId);
        if (StringUtils.isBlank(promoTokenInRedis) || !StringUtils.equals(promoToken,promoTokenInRedis)){
            return ResponseVO.fail("promoToken校验失败！");
        }

        //库存售罄判断
        if (redisTemplate.hasKey(PROMO_STOCK_INVALID_PROMOID_+promoId)){
            return ResponseVO.fail("库存不足");
        }
        Future future = executorService.submit(() -> {
            //初始化订单流水状态
            String uuid = promoServiceApi.initStockLog(promoId, amount);
            if (StringUtils.isEmpty(uuid)) {
                log.info("初始化订单流水状态失败！");
                throw new GunsException(GunsExceptionEnum.DATABASE_ERROR);
            }
            try {
                promoServiceApi.transactionSavePromoOrderVO(promoId,Integer.valueOf(userId),amount,uuid);
            } catch (Exception e) {
                throw new GunsException(GunsExceptionEnum.DATABASE_ERROR);
            }

        });

        try {
            future.get();
        } catch (InterruptedException e) {
            return ResponseVO.fail("下单失败！");
        } catch (ExecutionException e) {
            return ResponseVO.fail("执行失败！");
        }


        return ResponseVO.success("下单成功！");
    }

    /**
     * 将库存信息发布到缓存中
     * @return
     */
    @RequestMapping(value = "/publishPromoStock",method = RequestMethod.GET)
    @ResponseBody
    public ResponseVO publishStock(@RequestParam(required = false, name = "cinemaId") Integer cinemaId){
        Boolean flag = promoServiceApi.publishPromoStock(cinemaId);
        if (flag) {
            log.info("将秒杀缓存信息发布到缓存中成功！");
            return ResponseVO.success("发布成功!");
        }
        return ResponseVO.fail("将缓存信息发布到缓存中失败！");
    }


    /**
     * 获取秒杀令牌接口
     * @param promoId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/generateToken",method = RequestMethod.GET)
    @ResponseBody
    public ResponseVO generateToken(@RequestParam(required = true, name = "promoId") Integer promoId,
                                    HttpServletRequest request,HttpServletResponse response){
        ResponseVO responseVO = tokenUtil.getUserId(request, response);
        String userId = (String) responseVO.getMsg();
        if (responseVO == null || responseVO.getStatus() != ResponseStatus.success.getIndex() || StringUtils.isEmpty(userId)) {
            log.info("获取用户失败！请用户重新登录！responseVO:{}",JSON.toJSONString(responseVO));
            return ResponseVO.expire();
        }

        PromoVO promoVO = promoServiceApi.getPromoById(promoId);
        if (promoVO == null || promoVO.getUuid() == null) {
            log.info("promoId参数有误！promoId:{}",promoId);
            return ResponseVO.fail("参数有误！");
        }
        if (promoVO.getStatus() == null || PromoStatus.ING.getIndex() != promoVO.getStatus()) {
            log.info("当前活动不在活动时间范围之内,promoVO:{}",JSON.toJSONString(promoVO));
            return ResponseVO.fail("活动不在进行中！");
        }
        Long newCount = redisTemplate.opsForValue().increment(PROMO_TOKEN_LIMIT_COUNT_KEY+promoId, -1);
        if (newCount.intValue() < 0) {
            log.info("秒杀令牌已经发完了！promoId:{}",promoId);
            return ResponseVO.fail("系统繁忙！");
        }

        String promoToken = promoServiceApi.generatePromoToken(promoId, Integer.valueOf(userId));
        if (StringUtils.isBlank(promoToken)) {
            return ResponseVO.fail("token获取失败！");
        }
        return ResponseVO.success(new String(promoToken));
    }



}