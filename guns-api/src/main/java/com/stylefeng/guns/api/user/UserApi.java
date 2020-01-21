package com.stylefeng.guns.api.user;

import com.stylefeng.guns.api.user.vo.UserWalletOperateVO;
import com.stylefeng.guns.api.user.vo.UserWalletVO;

import java.math.BigDecimal;

/**
 * @author: jia.xue
 * @create: 2019-06-11 16:43
 * @Description
 **/
public interface UserApi {
    int login(String username, String password) ;

    Boolean register(UserModel userModel);

    Boolean checkUsername(String username);

    UserInfoModel getUserInfo(int uuid);

    UserInfoModel update(UserInfoModel userInfoModel);

    /**
     * 1，修改用户余额
     * 2，记录用户钱包流水日志
     * @return
     */
    UserWalletVO updateUserWallet(UserWalletOperateVO walletOperateVO);

}