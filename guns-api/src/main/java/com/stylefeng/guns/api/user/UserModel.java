package com.stylefeng.guns.api.user;

import java.io.Serializable;

/**
 * @author: jia.xue
 * @create: 2019-06-11 17:53
 * @Description
 **/
public class UserModel implements Serializable {

    private static final long serialVersionUID = -7466602031966052478L;
    private String username;

    private String password;
    private String email;
    private String address;
    private String mobile;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}