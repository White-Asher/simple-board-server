package com.whiteasher.simpleboard.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.whiteasher.simpleboard.auth.entity.RoleType;
import com.whiteasher.simpleboard.auth.token.AuthToken;
import com.whiteasher.simpleboard.auth.token.AuthTokenProvider;
import com.whiteasher.simpleboard.auth.util.CookieUtil;
import com.whiteasher.simpleboard.auth.util.HeaderUtil;
import com.whiteasher.simpleboard.auth.util.RedisUtil;
import com.whiteasher.simpleboard.config.TokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final AuthTokenProvider tokenProvider;
    private final RedisUtil redisUtil;
    private final TokenProperties tokenProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("CALL : TokenAuthenticationFilter - doFilterInternal()");
        log.info("INPUT URL : {}", request.getRequestURL().toString());

        String refreshToken = null;

        try {
            log.info("STRING request : {}", request.toString());
            // Header 에서 accessToken 정보를 가져온다.
            String accessToken = HeaderUtil.getAccessToken(request,
                    tokenProperties.getAuth().getAccessTokenHeaderName(),
                    tokenProperties.getAuth().getAccessTokenHeaderPrefix());
            // 토큰이 없으면 필터에서 검증하지 않고 통과
            if (accessToken != null) {
                AuthToken authAccessToken = tokenProvider.convertAuthToken(accessToken);
                log.info("TokenAuthenticationFilter | authAccessToken : {}", authAccessToken);
                // Redis 에서 AccessToken을 확인했을 때 존재한다면 블랙리스트 토큰
                if(redisUtil.getData(authAccessToken.getToken()) != null) {
                    throw new JwtException("블랙리스트 토큰");
                }
                // 요효성 검증은 getAuthentication 메서드를 통해 검증됨.
                Authentication accessAuthentication = tokenProvider.getAuthentication(authAccessToken);
                // 유효성을 만족한다면 SecurityContextHolder에 Accesstoken accessAuthentication 정보를 적재함.
                SecurityContextHolder.getContext().setAuthentication(accessAuthentication);
            }
            filterChain.doFilter(request, response);
        }

        // 엑세스 토큰만료되면 리프래쉬 토큰을 확인한 후 로직을 처리함.
        catch (ExpiredJwtException e) {
            log.info("TokenAuthenticationFilter | ExpiredJwtException : 엑세스 토큰 만료됨. 리프래쉬 토큰 가져오기");
            e.printStackTrace();

            // refresh token 가져오기
            refreshToken = CookieUtil.getCookie(request, tokenProperties.getAuth().getRefreshTokenName())
                    .map(Cookie::getValue)
                    .orElse((null));

            if (refreshToken == null) {
                throw new JwtException("리프래쉬 토큰이 없습니다.");
            }
            log.info("TokenAuthenticationFilter | ExpiredJwtException | refreshToken : {}", refreshToken);

            AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);
            log.info("TokenAuthenticationFilter | ExpiredJwtException |  authRefreshToken : {}", authRefreshToken);

            Claims refreshTokenClaims = tokenProvider.getClaims(authRefreshToken);
            log.info("TokenAuthenticationFilter | ExpiredJwtException | claims : {}", refreshTokenClaims);

//            if (refreshTokenClaims != null) {
//                throw new JwtException("토큰 만료되지 않음");
//            }

            String userId = refreshTokenClaims.getSubject();
            RoleType roleType = RoleType.of(refreshTokenClaims.get("role", String.class));
            log.info("TokenAuthenticationFilter | ExpiredJwtException | userId : {}", userId);
            log.info("TokenAuthenticationFilter | ExpiredJwtException | roleType : {}", roleType);

            // userId refresh token 으로 DB 확인
            String userRefreshToken = redisUtil.getData(userId);
            log.info("TokenAuthenticationFilter | ExpiredJwtException | userRefreshToken : {} ", userRefreshToken);
            if (userRefreshToken == null) {
                CookieUtil.deleteCookie(request, response, tokenProperties.getAuth().getRefreshTokenName());
                throw new JwtException("Refresh Token DB에 없음");
            }

            // RefreshToken 존재하므로 AccessToken 재발급
            Date now = new Date();
            AuthToken newAccessToken = tokenProvider.createAuthToken(
                    userId,
                    roleType.getCode(),
                    new Date(now.getTime() + tokenProperties.getAuth().getAccessTokenExpiry())
            );

            Authentication newAccessTokenAuthentication = tokenProvider.getAuthentication(newAccessToken);
            log.info("TokenAuthenticationFilter | ExpiredJwtException | new AccessToken : {}", newAccessToken.getToken());
            // 새로운 AccessToken을 SecurityContextHolder에 적재함.
            SecurityContextHolder.getContext().setAuthentication(newAccessTokenAuthentication);

            // 새로운 AccessToken을 Header Authorization에 적재함.
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Authorization", "Bearer " + newAccessToken.getToken());

            // Refresh Token 만료시간 계산
            long validTime = authRefreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

            // Refresh Token 기간이 3일 이하로 남은 경우, Refresh Token 갱신
            if (validTime <= 1000L * 60L * 60L * 24L * 3L) {
                // Refresh Token 만료시간설정
                long refreshTokenExpiry = tokenProperties.getAuth().getRefreshTokenExpiry();
                authRefreshToken = tokenProvider.createAuthToken(
                        userId,
                        roleType.getCode(),
                        new Date(now.getTime() + refreshTokenExpiry)
                );

                // Redis 에 refresh 토큰 업데이트
                redisUtil.setDataExpire(userId, authRefreshToken.getToken(), refreshTokenExpiry);

                // 만료시간 설정 후 쿠키 적재
                int cookieMaxAge = (int) refreshTokenExpiry;
                CookieUtil.deleteCookie(request, response, tokenProperties.getAuth().getRefreshTokenName());
                CookieUtil.addCookie(response, tokenProperties.getAuth().getRefreshTokenName(), authRefreshToken.getToken(), cookieMaxAge);
            }

        }

        catch (Exception e) {
            // 예외 발생하면 바로 setErrorResponse 호출
            e.printStackTrace();
            setErrorResponse(request, response, e);
        }
    }

    public void setErrorResponse(HttpServletRequest req, HttpServletResponse res, Throwable ex) throws IOException {
        // 필터에서 예외 발생시 리턴 정의
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setStatus(HttpServletResponse.SC_OK);

        log.info("JwtExceptionFilter ErrorResponse : {}", ex.getMessage());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.writeValue(res.getOutputStream(), ex.getMessage());
    }


}
