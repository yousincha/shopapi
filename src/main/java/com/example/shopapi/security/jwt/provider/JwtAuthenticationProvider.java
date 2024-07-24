package com.example.shopapi.security.jwt.provider;

import com.example.shopapi.security.jwt.token.JwtAuthenticationToken;
import com.example.shopapi.security.jwt.util.JwtTokenizer;
import com.example.shopapi.security.jwt.util.LoginInfoDto;
import com.example.shopapi.security.jwt.util.AdminLoginInfoDto;
import com.example.shopapi.security.jwt.util.AdminLoginUserDto;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenizer jwtTokenizer;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        Claims claims = jwtTokenizer.parseAccessToken(authenticationToken.getToken());
        String email = claims.getSubject();
        List<GrantedAuthority> authorities = getGrantedAuthorities(claims);

        if (claims.get("memberId") != null) {
            Long memberId = claims.get("memberId", Long.class);
            String name = claims.get("name", String.class);

            LoginInfoDto loginInfo = new LoginInfoDto();
            loginInfo.setMemberId(memberId);
            loginInfo.setEmail(email);
            loginInfo.setName(name);

            return new JwtAuthenticationToken(authorities, loginInfo, null);
        } else if (claims.get("adminId") != null) {
            Long adminId = claims.get("adminId", Long.class);
            String name = claims.get("name", String.class);

            AdminLoginInfoDto adminLoginInfo = new AdminLoginInfoDto();
            adminLoginInfo.setId(adminId);
            adminLoginInfo.setEmail(email);

            return new JwtAuthenticationToken(authorities, adminLoginInfo, null);
        } else {
            throw new AuthenticationException("Invalid token") {};
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(Claims claims) {
        List<String> roles = (List<String>) claims.get("roles");
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(() -> role);
        }
        return authorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
