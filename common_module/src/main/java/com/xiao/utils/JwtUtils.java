package com.xiao.utils;

import com.xiao.entity.Payload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.UUID;

public class JwtUtils {

    private static final  String JWT_PAYLOAD_USER_KEY = "user";

    /**
     * 私钥加密 token
     * @param userInfo 载荷中的信息
     * @param privateKey 私钥
     * @param expire 过期时间， 分钟
     * @return
     */
    public static String generateTokenExpireInMinutes(Object userInfo, PrivateKey privateKey, int expire){

        System.out.println(privateKey);
        if(userInfo == null){
            return null;
        }
        String token = Jwts.builder()
                .claim(JWT_PAYLOAD_USER_KEY, JsonUtils.toString(userInfo))
                .setId(createJTI())
                .setExpiration(DateTime.now().plusMinutes(expire).toDate())
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();

        return token;
    }

    /**
     * 私钥加密 token
     * @param userInfo 载荷中的信息
     * @param privateKey 私钥
     * @param expire 过期时间，秒
     * @return
     */
    public static String generateTokenExpireInSeconds(Object userInfo, PrivateKey privateKey, int expire){

        if(userInfo == null){
            return null;
        }
        String token = Jwts.builder()
                .claim(JWT_PAYLOAD_USER_KEY, JsonUtils.toString(userInfo))
                .setId(createJTI())
                .setExpiration(DateTime.now().plusSeconds(expire).toDate())
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();

        return token;
    }

    private static String createJTI(){
        return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
    }


    /**
     * 公钥解析 token
     * @param token
     * @param publicKey
     * @return
     */
    public static Jws<Claims> parserToken(String token, PublicKey publicKey){

        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);

    }

    /**
     * 获取 token 中的载荷信息
     * @param token
     * @param publicKey
     * @param userType
     * @param <T>
     * @return
     */
    public static <T> Payload<T> getInfoFromToken(String token, PublicKey publicKey, Class<T> userType) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        Payload<T> claims = new Payload<>();
        claims.setId(body.getId());
        claims.setUserInfo(JsonUtils.toBean(body.get(JWT_PAYLOAD_USER_KEY).toString(), userType));
        claims.setExpiration(body.getExpiration());
        return claims;
    }

    public static <T> Payload<T> getInfoFromToken(String token, PublicKey publicKey) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        Payload<T> claims = new Payload<>();
        claims.setId(body.getId());
        claims.setExpiration(body.getExpiration());
        return claims;
    }


}
