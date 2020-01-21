package com.stylefeng.guns.api.alipay;

import com.stylefeng.guns.api.alipay.vo.AlipayInfoVO;
import com.stylefeng.guns.api.alipay.vo.AlipayResultVO;

public interface AlipayServiceApi {

    //支付宝支付对接接口


    //生成二维码
    AlipayInfoVO getQRCode(String orderId);

    //查询订单状态
    AlipayResultVO getOrderStatus(String orderId);


}
