package com.stylefeng.guns.test;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.api.user.UserApi;
import com.stylefeng.guns.api.user.UserModel;
import com.stylefeng.guns.api.user.vo.UserWalletOperateVO;
import com.stylefeng.guns.api.user.vo.UserWalletVO;
import com.stylefeng.guns.core.constant.WalletOperType;
import com.stylefeng.guns.rest.UserApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;

/**
 * @author: jia.xue
 * @create: 2019-08-05 14:44
 * @Description
 **/


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UserApplication.class)
public class Usertest {

    @Autowired
    private UserApi userApi;

    @Test
    public void test01(){
        UserModel userModel = new UserModel();

        userModel.setUsername("test01");
        userModel.setPassword("test01");
        userModel.setAddress("软件新城");

        userModel.setEmail("291133@qq.com");
        userModel.setMobile("19822222222");

        Boolean register = userApi.register(userModel);
        System.out.println("register: "+register);
    }


    @Test
    public void test02(){
        UserWalletOperateVO walletOperateVO = new UserWalletOperateVO();
        walletOperateVO.setUserId(12);
        walletOperateVO.setWalletOperateType(WalletOperType.SUBSTRACT.getIndex());
        walletOperateVO.setReqAmount(new BigDecimal(1));
        walletOperateVO.setOrderId("OD23234231wef2424");
        UserWalletVO userWalletVO = userApi.updateUserWallet(walletOperateVO);
        System.out.println(JSON.toJSONString(userWalletVO));
    }
}