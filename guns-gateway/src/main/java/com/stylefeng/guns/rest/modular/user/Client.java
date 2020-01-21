package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.user.UserApi;
import org.springframework.stereotype.Component;

/**
 * @author: jia.xue
 * @create: 2019-06-11 17:22
 * @Description
 **/
@Component
public class Client {

    @Reference(interfaceClass = UserApi.class)
    private UserApi userApi;

    public void  run(){
        userApi.login("admin","ciggar");
    }
}