package com.stylefeng.guns;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.api.alipay.AlipayServiceApi;
import com.stylefeng.guns.api.alipay.vo.AlipayInfoVO;
import com.stylefeng.guns.api.alipay.vo.AlipayResultVO;
import com.stylefeng.guns.rest.AlipayApplication;
import com.stylefeng.guns.rest.modular.alipay.AlipayServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author: jia.xue
 * @create: 2019-07-15 15:11
 * @Description
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {AlipayApplication.class,AlipayServiceImpl.class},webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlipayServiceTest {

    @Autowired
    private AlipayServiceApi alipayService;

    @Test
    public void testGetQRCode(){
        AlipayInfoVO qrCode = alipayService.getQRCode("11");
        System.out.println(JSON.toJSONString(qrCode));
    }


    @Test
    public void testQueryStatus(){
        AlipayResultVO orderStatus = alipayService.getOrderStatus("11");
        System.out.println(JSON.toJSONString(orderStatus));
    }
}