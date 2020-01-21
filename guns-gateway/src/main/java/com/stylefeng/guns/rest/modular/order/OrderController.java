package com.stylefeng.guns.rest.modular.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.alipay.AlipayServiceApi;
import com.stylefeng.guns.api.alipay.vo.AlipayInfoVO;
import com.stylefeng.guns.api.alipay.vo.AlipayResultVO;
import com.stylefeng.guns.api.order.OrderServiceApi;
import com.stylefeng.guns.api.order.vo.OrderVO;
import com.stylefeng.guns.core.constant.ResponseStatus;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.rest.modular.auth.util.TokenUtil;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author: jia.xue
 * @create: 2019-06-16 22:20
 * @Description
 **/
@RestController
@RequestMapping(value = "/order")
public class OrderController {

    private static final String IMG_PRE = "http://www.duolaima.com";
    /**
     * 1、用户下单购票接口
     * @param fieldId
     * @param soldSeats
     * @param seatsName
     * @return
     */

    @Reference(interfaceClass = OrderServiceApi.class,check = false)
    private OrderServiceApi orderService;

    @Reference(interfaceClass = AlipayServiceApi.class,check = false)
    private AlipayServiceApi alipayService;

    @Autowired
    private TokenUtil tokenUtil;

    private transient static final  Logger logger = LoggerFactory.getLogger(OrderController.class);
    @RequestMapping(value = "/buyTickets",method = RequestMethod.POST)
    public ResponseVO buyTickets(Integer fieldId, String soldSeats, String seatsName, HttpServletRequest request, HttpServletResponse response){
        OrderVO orderVO;
        if (fieldId == null || StringUtils.isBlank(soldSeats) || StringUtils.isBlank(seatsName)) {
            return ResponseVO.fail("请求参数非法！");
        }
        try {
            Boolean trueSeats = orderService.isTrueSeats(soldSeats, fieldId + "");
            if (!trueSeats){
                return ResponseVO.fail("该座位不存在");
            }
            Boolean notSoldSeats = orderService.isNotSoldSeats(fieldId + "", soldSeats);
            if (!notSoldSeats){
                return ResponseVO.fail("该座位已被购买");
            }
            ResponseVO responseVO = tokenUtil.getUserId(request, response);
            if (ResponseStatus.expire.getIndex() == responseVO.getStatus()){
                return ResponseVO.expire();
            }
            String userId = responseVO.getMsg();

            orderVO = orderService.saveOrderInfo(fieldId, soldSeats, seatsName, Integer.valueOf(userId),null);
            if (orderVO == null) {
                return ResponseVO.fail("下单失败");
            }
            return ResponseVO.success(orderVO);
        } catch (Exception e) {
            logger.info("下单异常！fieldId:{}, soldSeats:{}, seatsName:{}",fieldId,soldSeats,seatsName);
            e.printStackTrace();
            return ResponseVO.exception("系统出现异常，请联系管理员");
        }
    }


    @RequestMapping(value = "/getOrderInfo",method = RequestMethod.POST)
    public ResponseVO getOrderInfo(@RequestParam(name ="nowPage",required = false,defaultValue = "1") Integer nowPage,
                                   @RequestParam(name ="pageSize",required = false,defaultValue = "5")Integer pageSize,
                                   HttpServletRequest request, HttpServletResponse response){

        try {
            ResponseVO responseVO = tokenUtil.getUserId(request, response);
            if (ResponseStatus.expire.getIndex() == responseVO.getStatus()) {
                return ResponseVO.expire();
            }
            String userIds = responseVO.getMsg();
            if (StringUtils.isBlank(userIds)) {
                return ResponseVO.fail("用户未登录");
            }
            logger.info("userId: {}.....",userIds);
            Integer userId = Integer.valueOf(userIds);
            List<OrderVO> vos = orderService.getOrdersByUserId(userId, nowPage, pageSize);
            if (CollectionUtils.isEmpty(vos)) {
                return ResponseVO.fail("订单列表为空");
            }
            return ResponseVO.success(vos);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            logger.info("查询订单列表异常！参数：userId{}, nowPage:{}, pageSize:{}",tokenUtil.getUserId(request, response),nowPage,pageSize);
            return ResponseVO.exception("查询订单列表异常");
        }

    }

    /**
     * 获取支付信息（二维码地址） 接口
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/getPayInfo",method = RequestMethod.POST)
    public ResponseVO getPayInfo(@RequestParam("orderId") String orderId,HttpServletRequest request,HttpServletResponse response){
        ResponseVO responseVO = tokenUtil.getUserId(request, response);
        if (ResponseStatus.expire.getIndex() == responseVO.getStatus()) {
            return ResponseVO.expire();
        }
        String userIds = responseVO.getMsg();
        if (StringUtils.isBlank(userIds)) {
            return ResponseVO.fail("用户未登录");
        }
        AlipayInfoVO qrCode = alipayService.getQRCode(orderId);

        return ResponseVO.success(IMG_PRE,qrCode);
    }

    /**
     * 获取支付结果接口
     * @param orderId
     * @param tryNums
     * @return
     */
    @RequestMapping(value = "/getPayResult",method = RequestMethod.POST)
    public ResponseVO getPayResult(@RequestParam("orderId") String orderId,
                                   @RequestParam(name = "tryNums",required = false,defaultValue = "1") String tryNums,
                                   HttpServletRequest request,HttpServletResponse response){

        ResponseVO responseVO = tokenUtil.getUserId(request, response);
        if (ResponseStatus.expire.getIndex() == responseVO.getStatus()) {
            return ResponseVO.expire();
        }
        String userId = responseVO.getMsg();
        if (StringUtils.isBlank(userId)) {
            return ResponseVO.fail("用户未登录");
        }
        if (Integer.valueOf(tryNums) >3) {
            return ResponseVO.fail("订单支付失败，请稍后重试");
        }
        AlipayResultVO vo = alipayService.getOrderStatus(orderId);

        if (vo == null || ToolUtil.isEmpty(vo.getOrderId())) {
            return ResponseVO.fail("支付失败！");
        }
        return ResponseVO.success(vo);
    }



}