package com.stylefeng.guns.api.user.vo;



import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: jia.xue
 * @create: 2019-08-08 16:07
 * @Description
 **/
@Data
public class UserWalletVO implements Serializable {

    //用户id
    private Integer userId;

    //钱包余额 暂时不会返回，涉及到事务问题，需要主动去查
    private BigDecimal currentCurrency;

    //钱包状态
    private Integer status;
}