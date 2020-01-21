package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.user.UserApi;

import com.stylefeng.guns.api.user.UserInfoModel;
import com.stylefeng.guns.api.user.UserModel;
import com.stylefeng.guns.core.constant.ResponseStatus;
import com.stylefeng.guns.rest.modular.auth.util.TokenUtil;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: jia.xue
 * @create: 2019-06-11 21:44
 * @Description
 **/
@RequestMapping("/user")
@RestController
public class UserController {

    private transient static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Reference(interfaceClass = UserApi.class,check = false)
    private UserApi userApi;

    @Autowired
    private TokenUtil tokenUtil;

    /**
     * 用户注册接口
     * @param userModel
     * @return
     */
    @RequestMapping("/register")
    public ResponseVO register(UserModel userModel){
        if (StringUtils.isBlank(userModel.getUsername())) {
            return ResponseVO.fail("用户名不能为空！");
        }
        if (StringUtils.isBlank(userModel.getPassword())) {
            return ResponseVO.fail("密码不能为空！");
        }
        Boolean checkUsername = userApi.checkUsername(userModel.getUsername());
        if (!checkUsername) {
            return ResponseVO.fail("用户已经存在！");
        }
        Boolean aBoolean = false;
        try {
            aBoolean = userApi.register(userModel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.fail("用户注册失败！");
        }
        if (aBoolean) {
            logger.info("注册成功！username:[{}]",userModel.getUsername());
            return ResponseVO.success("用户注册成功！");
        }else {
            logger.info("注册失败！username:[{}]",userModel.getUsername());
            return ResponseVO.fail("用户注册失败！");
        }
    }

    @RequestMapping(value = "/login")
    public ResponseVO login(String username,String password){

        if (StringUtils.isAnyBlank(username,password)) {
            return ResponseVO.fail("用户名或密码不能为空！");
        }

        int login = userApi.login(username, password);
        if (login > 0){
            logger.info("登录成功！用户id：{}, 用户名：{}", login, username);
            return ResponseVO.success(login);
        }
        return ResponseVO.fail("登录失败！");
    }


    @RequestMapping(value = "/check",method = RequestMethod.POST)
    public ResponseVO check(String username){
        if (StringUtils.isBlank(username)) {
            return ResponseVO.fail("用户名不能为空！");
        }
        Boolean aBoolean = userApi.checkUsername(username);
        if (aBoolean) {
            return ResponseVO.success("用户名不存在！");
        }else {
            return ResponseVO.fail("用户名已经注册！");
        }
    }


    @RequestMapping(value = "/getUserInfo",method = RequestMethod.GET)
    public ResponseVO getUserInfo(HttpServletRequest request, HttpServletResponse response){
        ResponseVO responseVO = tokenUtil.getUserId(request, response);
        if (ResponseStatus.expire.getIndex() == responseVO.getStatus()) {
            return ResponseVO.expire();
        }
        String userId = responseVO.getMsg();
        if (StringUtils.isNotBlank(userId)){
            Integer id = Integer.valueOf(userId);
            UserInfoModel userInfo = userApi.getUserInfo(id);
            if (userInfo != null) {
                return ResponseVO.success(userInfo);
            }else {
                return ResponseVO.fail("查询用户信息失败！");
            }
        }else {
            return ResponseVO.exception("用户未登录！");
        }
    }

    @RequestMapping(value = "/updateUserInfo",method = RequestMethod.POST)
    public ResponseVO updateUserInfo(UserInfoModel userInfoModel,HttpServletRequest request,HttpServletResponse response){
        ResponseVO responseVO = tokenUtil.getUserId(request, response);
        if (ResponseStatus.expire.getIndex() == responseVO.getStatus()) {
            return ResponseVO.expire();
        }
        String userId = responseVO.getMsg();
        if (StringUtils.isNotBlank(userId)){
            Integer id = Integer.valueOf(userId);
            if (userInfoModel.getUuid() != id) {
                return ResponseVO.exception("请修改您个人的信息！");
            }

            UserInfoModel userInfoMode = userApi.update(userInfoModel);
            if (userInfoMode != null) {
                return ResponseVO.success(userInfoMode);
            }else {
                return ResponseVO.fail("用户信息修改失败！");
            }
        }else {
            return ResponseVO.exception("用户未登录！");
        }
    }

    @RequestMapping(value = "/logout" , method = RequestMethod.GET)
    public ResponseVO logout(HttpServletRequest request, HttpServletResponse response) {
        Boolean isSuccess = tokenUtil.delTokenFromCache(request, response);
        if (isSuccess) {
            return ResponseVO.success("注销成功！");
        }else {
            return ResponseVO.fail("注销失败！");
        }
    }




}