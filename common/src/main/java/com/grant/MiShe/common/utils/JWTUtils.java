package com.grant.MiShe.common.utils;

import com.grant.MiShe.common.exception.LeaseException;
import com.grant.MiShe.common.result.ResultCodeEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JWTUtils {

    private static SecretKey secretKey = Keys.hmacShaKeyFor("dKmP4WcYTTykYpYzeG53xxDsXVxy3j7K".getBytes());

    public static String creatJWT(Long userId,String userName){


        String jwt = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .setSubject("LOGIN_USER")
                .claim("userId", userId)
                .claim("userName", userName)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return jwt;

    }

    public static void parseToken(String token){

        try {
            JwtParser build = Jwts.parserBuilder().setSigningKey(secretKey).build();
            build.parseClaimsJwt(token);
        }catch (ExpiredJwtException e){
            throw new LeaseException(ResultCodeEnum.TOKEN_EXPIRED);
        }catch (JwtException e){
            throw new LeaseException(ResultCodeEnum.TOKEN_INVALID);
        }

    }

    public static void main(String[] args) {
        System.out.println(creatJWT(1L,"hhhh"));
    }
}
