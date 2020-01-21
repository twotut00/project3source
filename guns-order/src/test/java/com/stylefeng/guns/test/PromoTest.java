package com.stylefeng.guns.test;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.api.order.OrderServiceApi;
import com.stylefeng.guns.api.promo.vo.PromoVO;
import com.stylefeng.guns.core.constant.PromoStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author: jia.xue
 * @create: 2019-08-06 17:44
 * @Description
 **/
public class PromoTest extends BaseTest {

    @Autowired
    private OrderServiceApi orderServiceApi;
//    @Test
//    public void test01(){
//        List<PromoVO> promoVOS = orderServiceApi.getAllPromo(PromoStatus.ING.getIndex());
//        System.out.println(JSON.toJSONString(promoVOS));
//    }
}