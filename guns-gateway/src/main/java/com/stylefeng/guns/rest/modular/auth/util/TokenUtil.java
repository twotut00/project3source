package com.stylefeng.guns.rest.modular.auth.util;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.core.base.tips.ErrorTip;
import com.stylefeng.guns.core.util.RenderUtil;
import com.stylefeng.guns.rest.common.exception.BizExceptionEnum;
import com.stylefeng.guns.rest.config.properties.JwtProperties;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author: jia.xue
 * @create: 2019-06-24 21:56
 * @Description
 **/
@Slf4j
@Component
public class TokenUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtProperties jwtProperties;

    private static final Integer expireTime = 60 * 60;


    public ResponseVO getUserId(HttpServletRequest request, HttpServletResponse response) {
        final String requestHeader = request.getHeader(jwtProperties.getHeader());
        String authToken = null;
        String userId = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            authToken = requestHeader.substring(7);

            //通过token获取userId
            try {
                userId = jwtTokenUtil.getUsernameFromToken(authToken);
            }catch (ExpiredJwtException e){
                e.printStackTrace();
                ResponseVO responseVO = ResponseVO.expire();

                return responseVO;
            }catch (SignatureException e){
                e.printStackTrace();
                ResponseVO responseVO = ResponseVO.fail("签名错误！");
                log.info("签名已经过期！authToken:{}",authToken);
                return responseVO;
            }catch (Exception e) {
                e.printStackTrace();
                ResponseVO responseVO = ResponseVO.exception("服务器错误！");
                return responseVO;
            }
            if (StringUtils.isBlank(userId)) {
                return null;
            } else {
                //将userId存入缓存
                redisTemplate.opsForValue().set(authToken, userId,expireTime,TimeUnit.SECONDS);
            }
            //验证token是否过期,包含了验证jwt是否正确
            try {
                boolean flag = jwtTokenUtil.isTokenExpired(authToken);
                if (flag) {
                    RenderUtil.renderJson(response, new ErrorTip(BizExceptionEnum.TOKEN_EXPIRED.getCode(), BizExceptionEnum.TOKEN_EXPIRED.getMessage()));
                    return ResponseVO.expire();
                }
            } catch (JwtException e) {
                //有异常就是token解析失败
                RenderUtil.renderJson(response, new ErrorTip(BizExceptionEnum.TOKEN_ERROR.getCode(), BizExceptionEnum.TOKEN_ERROR.getMessage()));
                return null;
            }
            return ResponseVO.success(userId);
        } else {
            return null;
        }
    }

    public Boolean delTokenFromCache(HttpServletRequest request,HttpServletResponse response){
        final String requestHeader = request.getHeader(jwtProperties.getHeader());
        Boolean isDelete = false;

        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            String authToken = requestHeader.substring(7);
            isDelete = redisTemplate.delete(authToken);
        }
        return isDelete;
    }
}