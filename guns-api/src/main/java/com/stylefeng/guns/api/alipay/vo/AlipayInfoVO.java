package com.stylefeng.guns.api.alipay.vo;

import java.io.Serializable;

/**
 * @author: jia.xue
 * @create: 2019-06-19 10:49
 * @Description
 **/
public class AlipayInfoVO implements Serializable {

    private static final long serialVersionUID = 2753268966718259052L;
    private String orderId;
    private String QRCodeAddress;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getQRCodeAddress() {
        return QRCodeAddress;
    }

    public void setQRCodeAddress(String QRCodeAddress) {
        this.QRCodeAddress = QRCodeAddress;
    }
}