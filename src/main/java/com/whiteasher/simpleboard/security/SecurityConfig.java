package com.whiteasher.simpleboard.security;

import com.whiteasher.simpleboard.auth.exception.RestAuthenticationEntryPoint;
import com.whiteasher.simpleboard.auth.filter.TokenAuthenticationFilter;
import com.whiteasher.simpleboard.auth.handler.CustomLogoutSuccessHandler;
import com.whiteasher.simpleboard.auth.handler.OAuth2AuthenticationFailureHandler;
import com.whiteasher.simpleboard.auth.handler.OAuth2AuthenticationSuccessHandler;
import com.whiteasher.simpleboard.auth.handler.TokenAccessDeniedHandler;
import com.whiteasher.simpleboard.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.whiteasher.simpleboard.auth.service.CustomOAuth2UserService;
import com.whiteasher.simpleboard.auth.token.AuthTokenProvider;
import com.whiteasher.simpleboard.auth.util.RedisUtil;
import com.whiteasher.simpleboard.config.CorsProperties;
import com.whiteasher.simpleboard.config.TokenProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Slf4j
@Configuration
// @EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsProperties corsProperties;
    private final TokenProperties tokenProperties;
    private final AuthTokenProvider tokenProvider;
    private final CustomOAuth2UserService oAuth2UserService;
    private final TokenAccessDeniedHandler tokenAccessDeniedHandler;
    private final CustomLogoutSuccessHandler logoutSuccessHandler;
    private final RedisUtil redisUtil;

    private final String[] permitAllRequestURL = new String[]{
            "**",
    };

    private final String[] ignoreFilterRequestURL = new String[]{
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/proxy/**",
            "/error",
            "/api/v1/login",
            "/api/v1/users/resister"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                // CORS 정책
                .cors((corsConfigurer)->
                        corsConfigurer
                                .configurationSource(corsConfigurationSource())
                )
                /*
                스프링 시큐리티 세션 정책
                스프링 시큐리티가 생성하지 않고 기존것을 사용하지 않음 (JWT 토큰 방식 사용시 설정)
                 */
                .sessionManagement((session) ->
                        session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth ->
                        auth
//                                .requestMatchers("/management/**").hasRole("ADMIN")
                                .requestMatchers(permitAllRequestURL).permitAll()
                                //.requestMatchers("/api/v1/**").hasRole(Role.USER.name())
                                .anyRequest().authenticated())
                .exceptionHandling((handler) ->
                        handler
                                // 예외 발생시 RestAuthenticationEntryPoint 에서 받아서 처리함.
                                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                                // 접근 권한이 거부되면 실행되는 핸들러
                                .accessDeniedHandler(tokenAccessDeniedHandler)
                )

                // // 지정된 필터 앞에 커스텀 필터를 추가 (UsernamePasswordAuthenticationFilter 보다 먼저 실행된다)
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 ->
                        oauth2
                                .loginPage("/login")
                                .userInfoEndpoint(userInfo -> userInfo
                                        .userService(oAuth2UserService)
                                )
                                .authorizationEndpoint(authorization -> authorization
                                        .baseUri("/oauth2/authorization")
                                        .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                                )
                                .redirectionEndpoint(redirection -> redirection
                                        .baseUri("/*/oauth2/code/*")
                                )
                                .successHandler(oAuth2AuthenticationSuccessHandler())
                                .failureHandler(oAuth2AuthenticationFailureHandler())
                )
                .logout((logout) ->
                        logout
//                                .logoutUrl("/api/auth/logout")
                                .logoutSuccessHandler(logoutSuccessHandler)
                                .logoutSuccessUrl("/")
                );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> {
            web.ignoring()
                    .requestMatchers(ignoreFilterRequestURL);
                    //.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        };
    }
    /*
     * auth 매니저 설정
     * */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        log.info("SecurityConfig : authenticationManager method CALL");
        return auth.getAuthenticationManager();
    }

    /*
     * security 설정 시, 사용할 인코더 설정
     * */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * 토큰 필터 설정
     * */
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider, redisUtil, tokenProperties);
    }

    /*
     * 쿠키 기반 인가 Repository
     * 인가 응답을 연계 하고 검증할 때 사용.
     * */
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    /*
     * Oauth 인증 성공 핸들러
     * */
    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(
                tokenProvider,
                tokenProperties,
                oAuth2AuthorizationRequestBasedOnCookieRepository(),
                redisUtil
        );
    }

    /*
     * Oauth 인증 실패 핸들러
     * */
    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository());
    }

    /*
     * Cors 설정
     * */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfig = new CorsConfiguration();

        corsConfig.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
        corsConfig.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
        corsConfig.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(corsConfig.getMaxAge());
        corsConfig.addExposedHeader("Authorization");
        corsConfigSource.registerCorsConfiguration("/**", corsConfig);
        log.info("corsConfigSource : {}" , corsConfigSource);
        return corsConfigSource;
    }

}
