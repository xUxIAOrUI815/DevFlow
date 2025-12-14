package com.student.devflow.config;

import com.student.devflow.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行 OPTIONS 请求 跨域预检
        if("OPTIONS".equals(request.getMethod())){
            return true;
        }

        // 从 Header 获取 Token
        String token = request.getHeader("Authorization");

        // 校验 Token
        if(token != null && !token.isEmpty()){      // 防御性编程的经典写法
            Claims claims = JwtUtils.getClaimsByToken(token);
            if(claims != null) {
                // 登录成功
                // 把 userId 存入 request
                request.setAttribute("userId", claims.get("userId"));
                return true;
            }
        }

        // 失败
        response.setStatus(401);
        response.getWriter().write("{\"code\":401,\"message\":\"No Permission\"}");
        return false;
    }
}
