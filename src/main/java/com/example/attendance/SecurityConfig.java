package com.example.attendance;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http.csrf().disable()
//            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
//            .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
//        ;
//        return http.build();
//    }
//}

@Configuration
@EnableWebSecurity
public class SecurityConfig {
//    private final String[] allowedUrls = {"/", "/swagger-ui/**", "/v3/**","/user/new-user"};	// sign-up, sign-in 추가
private final String[] allowedUrls = {"/**"};	// sign-up, sign-in 추가
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //http.cors();
        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers(allowedUrls).permitAll()// requestMatchers의 인자로 전달된 url은 모두에게 허용
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
        ;
        return http.build();
    }
}