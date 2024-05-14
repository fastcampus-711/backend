package com.aptner.v3.global.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

// 현재 jwt 버전은 가장 최신버전인 0.12.3을 사용하고 있습니다.
@Component
public class JwtUtil {

    private SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        byte[] secretBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(secretBytes);
    }

    // 토큰에서 username을 가져옴
    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build().parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 토큰에서 role을 가져옴
    public String getRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
    // 토큰에서 tokenType을 가져옴
    public String getTokenType(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("tokenType", String.class);
    }

    // 토큰이 만료되었으면 true , 아니면 false
    public boolean isExpired(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    public String createToken(String tokenType,String username, String role, Long expireTime){
        return Jwts.builder()
                .claim("tokenType", tokenType)
                .claim("username",username)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(secretKey)// signWith 으로 해당 jwt 를 암호화
                .compact();
    }
}
