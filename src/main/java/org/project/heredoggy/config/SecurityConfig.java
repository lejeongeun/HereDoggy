package org.project.heredoggy.config;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.config.oauth.CustomOAuth2UserService;
import org.project.heredoggy.config.oauth.OAuth2SuccessHandler;
import org.project.heredoggy.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/hello").permitAll()

                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/members/free-posts/**", "/api/members/review-posts/**", "/api/members/missing-posts/**",  "/api/*-posts/*/comments", "/api/*-posts/*/likes/count").permitAll() // 게시물 조회, 댓글 조회, 좋아요 갯수는 비회원도 가능
                        .requestMatchers(HttpMethod.POST,"/api/shelters/login", "/api/admin/login", "/oauth2/authorization/**", "/login/oauth2/code/**", "/api/shelter-request").permitAll()
                        .requestMatchers("/api/dogs/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll() // push 전에 삭제하기(임시)
                        .requestMatchers("/api/reservations/**").permitAll() // push 전에 삭제하기(임시)
                        .requestMatchers("/error").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/shelters").permitAll() //보호소리스트 조회
                        .requestMatchers("/api/members/**").hasRole("USER")
                        .requestMatchers("/api/shelters/**").hasRole("SHELTER_ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("SYSTEM_ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://10.0.2.2:8080", "http://192.168.0.xxx:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}