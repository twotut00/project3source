package com.stylefeng.guns.api.alipay.vo;

import java.io.Serializable;

/**
 * @author: jia.xue
 * @create: 2019-06-19 10:50
 * @Description
 **/
public class AlipayResultVO implements Serializable {

    private static final long serialVersionUID = -4690649348144507584L;
    private String orderId;
    private Integer  orderStatus;
    private String orderMsg;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderMsg() {
        return orderMsg;
    }

    public void setOrderMsg(String orderMsg) {
        this.orderMsg = orderMsg;
    }
}