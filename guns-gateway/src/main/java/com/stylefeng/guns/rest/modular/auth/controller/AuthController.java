package com.stylefeng.guns.rest.modular.auth.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.user.UserApi;
import com.stylefeng.guns.rest.modular.auth.controller.dto.AuthRequest;
import com.stylefeng.guns.rest.modular.auth.controller.dto.AuthResponse;
import com.stylefeng.guns.rest.modular.auth.util.JwtTokenUtil;
import com.stylefeng.guns.rest.modular.auth.validator.IReqValidator;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.sql.SQLOutput;
import java.util.concurrent.*;

/**
 * 请求验证的
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:22
 */
@Slf4j
@RestController
public class AuthController {

    @Reference(interfaceClass = UserApi.class,check = false)
    private UserApi userApi;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Resource(name = "simpleValidator")
    private IReqValidator reqValidator;

    private static final Integer expireTime = 60 * 60;

    private static ExecutorService executorService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostConstruct
    public void init(){
        executorService = new ThreadPoolExecutor(10,100,0l,TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>());
    }

    @RequestMapping(value = "${jwt.auth-path}")
    public ResponseVO createAuthenticationToken(AuthRequest authRequest) {

//        userApi.login(authRequest.getUserName(),authRequest.getPassword());
        boolean validate = reqValidator.validate(authRequest);

//        boolean validate = true;
        final int userId = userApi.login(authRequest.getUserName(), authRequest.getPassword());
        if (userId != 0 ) {
            validate = true;
        }
        if (validate) {
            final String randomKey = jwtTokenUtil.getRandomKey();
            final String token = jwtTokenUtil.generateToken(userId + "", randomKey);
            executorService.execute(() ->{
                loginAfter(token,userId);
            });
            return ResponseVO.success(new AuthResponse(token,randomKey));
        } else {
            return ResponseVO.fail("用户名或密码错误！");
        }


    }

    private void loginAfter(String token, int userId) {
        redisTemplate.opsForValue().set(token,userId+"",expireTime,TimeUnit.SECONDS);
        log.info("登录数据存入缓存成功！ token:{},userId:{}",token,userId);
    }
}
