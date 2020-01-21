package com.stylefeng.guns.core.util;

import java.util.UUID;

/**
 * @author: jia.xue
 * @create: 2019-06-17 15:56
 * @Description
 **/
public class UUIDUtil {

    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","").substring(0,18);
    }
}