package com.example.shopapi.config;

import com.example.shopapi.security.jwt.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

// Spring Security 설정.
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationManagerConfig authenticationManagerConfig;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.disable())
                .csrf(csrf -> csrf.disable())
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.disable())
                .authorizeHttpRequests(httpRequests -> httpRequests
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers( "/members/signup", "/members/login", "/members/refreshToken","/members/findpassword","/members/resetpassword","/members/resettoken").permitAll()
                        .requestMatchers(  "/admins/login","/admins/signup","/admins/dashboard","/admins/refreshToken").permitAll()
                        .requestMatchers(GET, "/categories/**", "/products/**","/products2/**","/carts/**","/cartItems/**","/paymentInfos/**","/addresses/**").permitAll()
                        .requestMatchers(GET,"/**").hasAnyRole( "USER")
                        .requestMatchers(POST,"/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().hasAnyRole("USER", "ADMIN"))
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(customAuthenticationEntryPoint))
                .apply(authenticationManagerConfig);

        return httpSecurity.build();
    }

    // <<Advanced>> Security Cors로 변경 시도
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // 이거 빼면 된다

        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("Content-Type");
        config.addAllowedHeader("Authorization");
        config.addAllowedHeader("x-refresh-token"); // 추가된 부분

//        config.addAllowedOrigin("*");
        config.addAllowedMethod("*");
        config.setAllowedMethods(List.of("GET","POST","DELETE","PATCH","OPTION","PUT"));
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    // 암호를 암호화하거나, 사용자가 입력한 암호가 기존 암호랑 일치하는지 검사할 때 이 Bean을 사용
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
