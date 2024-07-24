package com.example.shopapi.security.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtTokenizer {

    private final byte[] accessSecret;
    private final byte[] refreshSecret;

    public final static Long ACCESS_TOKEN_EXPIRE_COUNT = 30 * 60 * 1000L; // 30 minutes
    public final static Long REFRESH_TOKEN_EXPIRE_COUNT = 7 * 24 * 60 * 60 * 1000L; // 7 days

    public JwtTokenizer(@Value("${jwt.secretKey}") String accessSecret, @Value("${jwt.refreshKey}") String refreshSecret) {
        this.accessSecret = accessSecret.getBytes(StandardCharsets.UTF_8);
        this.refreshSecret = refreshSecret.getBytes(StandardCharsets.UTF_8);
    }

    // Methods for creating tokens for members
    public String createMemberAccessToken(Long memberId, String email, String name, List<String> roles) {
        return createToken(memberId, email, name, roles, ACCESS_TOKEN_EXPIRE_COUNT, accessSecret, "memberId");
    }

    public String createMemberRefreshToken(Long memberId, String email, String name, List<String> roles) {
        return createToken(memberId, email, name, roles, REFRESH_TOKEN_EXPIRE_COUNT, refreshSecret, "memberId");
    }

    // Methods for creating tokens for admins
    public String createAdminAccessToken(Long adminId, String email, List<String> roles) {
        return createToken(adminId, email, roles, ACCESS_TOKEN_EXPIRE_COUNT, accessSecret, "adminId");
    }

    public String createAdminRefreshToken(Long adminId, String email, List<String> roles) {
        return createToken(adminId, email, roles, REFRESH_TOKEN_EXPIRE_COUNT, refreshSecret, "adminId");
    }

    // Overloaded method for cases where name is not required
    private String createToken(Long id, String email, List<String> roles,
                               Long expire, byte[] secretKey, String idClaim) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);
        claims.put(idClaim, id);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expire))
                .signWith(getSigningKey(secretKey))
                .compact();
    }

    private String createToken(Long id, String email, String name, List<String> roles,
                               Long expire, byte[] secretKey, String idClaim) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);
        claims.put(idClaim, id);
        claims.put("name", name);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expire))
                .signWith(getSigningKey(secretKey))
                .compact();
    }

    // Methods to extract member and admin IDs from token
    public Long getMemberIdFromToken(String token) {
        return getIdFromToken(token, "memberId", accessSecret);
    }

    public Long getAdminIdFromToken(String token) {
        return getIdFromToken(token, "adminId", accessSecret);
    }

    private Long getIdFromToken(String token, String idClaim, byte[] secretKey) {
        Claims claims = parseToken(token, secretKey);
        return claims.get(idClaim, Long.class);
    }

    public Claims parseAccessToken(String accessToken) {
        return parseToken(accessToken, accessSecret);
    }

    public Claims parseRefreshToken(String refreshToken) {
        return parseToken(refreshToken, refreshSecret);
    }

    private Claims parseToken(String token, byte[] secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static Key getSigningKey(byte[] secretKey) {
        return Keys.hmacShaKeyFor(secretKey);
    }
}
