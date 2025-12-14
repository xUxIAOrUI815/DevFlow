package com.student.devflow.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class JwtUtils {
    // 密钥
    private static final String SECRET = "DevFlowSecretKey123456";

    // 过期时间
    private static final long EXPIRATION = 86400000L;

    // 生成 token
    public static String generateToken(Long userId, String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId",userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    // 解析 Token 获取 Claims
    public static Claims getClaimsByToken(String token){
        try{
            return Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        }catch(Exception e){
            return null;    // 解析失败或过期
        }
    }
}
