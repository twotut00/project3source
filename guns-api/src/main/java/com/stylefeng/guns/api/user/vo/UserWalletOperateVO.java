package com.stylefeng.guns.api.user.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: jia.xue
 * @create: 2019-08-11 21:29
 * @Description 用户钱包操作VO
 **/
@Data
public class UserWalletOperateVO implements Serializable {
    private static final long serialVersionUID = 7734060515419735801L;

    private Integer userId;

    /**
     * {@link com.stylefeng.guns.core.constant.WalletOperType}
     */
    private Integer walletOperateType;

    private String orderId;

    private BigDecimal reqAmount;

}