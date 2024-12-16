package com.CloudBike.interceptor;

import com.CloudBike.constant.JwtClaimsConstant;
import com.CloudBike.constant.MessageConstant;
import com.CloudBike.context.BaseContext;
import com.CloudBike.properties.JwtProperties;
import com.CloudBike.result.Result;
import com.CloudBike.utils.JwtUtil;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

/**
 * 管理端拦截器
 * @author unique
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    private final JwtProperties jwtProperties;

    /**
     * 校验jwt
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        // 判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod))
        {
            // 当前拦截到的不是动态方法，直接放行
            return true;
        }

        // 1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getAdminTokenName());

        // 2、校验令牌
        try
        {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Integer empId = Integer.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());

            // 将当前用户的id在此次请求内共享
            BaseContext.setCurrentId(empId);

            log.info("当前员工id：", empId);
            // 3、通过，放行
            return true;
        } catch (Exception ex)
        {
            // 4、不通过，返回自定义数据
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            String jsonString = JSONObject.toJSONString(Result.error(MessageConstant.NOT_LOGIN));
            out.write(jsonString);
            out.flush();
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
    {
        // 清理用户
        BaseContext.removeId();
    }
}