package com.stylefeng.guns.rest.modular.auth.filter;

import com.stylefeng.guns.core.base.tips.ErrorTip;
import com.stylefeng.guns.core.util.RenderUtil;
import com.stylefeng.guns.rest.common.exception.BizExceptionEnum;
import com.stylefeng.guns.rest.config.properties.JwtProperties;
import com.stylefeng.guns.rest.modular.auth.util.JwtTokenUtil;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 对客户端请求的jwt token验证过滤器
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:04
 */
@Slf4j
public class AuthFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    //过期时间 设置为一个小时
    private static final Integer expireTime = 60 * 60;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request.getServletPath().equals("/" + jwtProperties.getAuthPath())) {
            chain.doFilter(request, response);
            return;
        }
        String ignoreUrl = jwtProperties.getIgnoreUrl();
        if (StringUtils.isNotBlank(ignoreUrl)) {
            String[] ignoreUrls = ignoreUrl.split(",");
            for (int i = 0; i < ignoreUrls.length; i++) {
                if (request.getServletPath().startsWith(ignoreUrls[i])) {
                    chain.doFilter(request,response);
                    return;
                }
            }
        }
        final String requestHeader = request.getHeader(jwtProperties.getHeader());
        String authToken = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            authToken = requestHeader.substring(7);
            String userId = redisTemplate.opsForValue().get(authToken);
            if (StringUtils.isBlank(userId)) {
                //通过token获取userId
                try {
                    userId = jwtTokenUtil.getUsernameFromToken(authToken);
                }catch (ExpiredJwtException e){
                    e.printStackTrace();
                    log.info("签名已经过期！authToken:{}",authToken);
                    ResponseVO responseVO = ResponseVO.expire();
                    RenderUtil.renderJson(response,responseVO);
                    return;
                }catch (SignatureException e){
                    e.printStackTrace();
                    ResponseVO responseVO = ResponseVO.fail("签名错误！");
                    RenderUtil.renderJson(response,responseVO);
                    return;
                }catch (Exception e) {
                    e.printStackTrace();
                    ResponseVO responseVO = ResponseVO.exception("服务器错误！");
                    RenderUtil.renderJson(response,responseVO);
                    return;
                }

                if (StringUtils.isBlank(userId)) {
                    logger.info("token签名以及redis缓存的token都过期了，请重新登录！authToken:{}",authToken);
                    ResponseVO responseVO = ResponseVO.expire();
                    RenderUtil.renderJson(response,responseVO);
                    return;
                }else {
                    //token 里面的userId 未过期，需要存入缓存
                    redisTemplate.opsForValue().set(authToken,userId,expireTime,TimeUnit.SECONDS);
                }
            }else {
                //刷新缓存
                redisTemplate.expire(authToken,expireTime,TimeUnit.SECONDS);
                logger.info("token:{}过期时间设置成功，过期时间为：{}",authToken,redisTemplate.getExpire(authToken));
            }
        } else {
            //header没有带Bearer字段
            RenderUtil.renderJson(response, new ErrorTip(BizExceptionEnum.TOKEN_ERROR.getCode(), BizExceptionEnum.TOKEN_ERROR.getMessage()));
            return;
        }
        chain.doFilter(request, response);
    }
}