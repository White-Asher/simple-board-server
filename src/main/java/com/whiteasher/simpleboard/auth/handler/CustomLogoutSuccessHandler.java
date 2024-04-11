package com.whiteasher.simpleboard.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.whiteasher.simpleboard.auth.token.AuthToken;
import com.whiteasher.simpleboard.auth.token.AuthTokenProvider;
import com.whiteasher.simpleboard.auth.util.CookieUtil;
import com.whiteasher.simpleboard.auth.util.HeaderUtil;
import com.whiteasher.simpleboard.auth.util.RedisUtil;
import com.whiteasher.simpleboard.common.response.CommonResponse;
import com.whiteasher.simpleboard.config.TokenProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final TokenProperties tokenProperties;
    private final RedisUtil redisUtil;
    private final AuthTokenProvider tokenProvider;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("CustomLogoutSuccessHandler :: onLogoutSuccess");
        try {
            // 헤더에서 accesstoken 가져오기
            String accessToken = HeaderUtil.getAccessToken(request,
                    tokenProperties.getAuth().getAccessTokenHeaderName(),
                    tokenProperties.getAuth().getAccessTokenHeaderPrefix());
            log.info("Logout access_token = {}", accessToken);
            if (accessToken == null) {
                throw new RuntimeException("엑세스 토큰이 없습니다.");
            }

            // Redis에 엑세스 토큰 블랙리스트 등록
            redisUtil.setDataExpire(accessToken, "true", tokenProperties.getAuth().getAccessTokenExpiry());
            log.info("엑세스 토큰 블랙리스트 등록");

            // context.holder accesstoken 정보 삭제.

            //Cookie에서 RefreshToken을 가져와 Redis에서 RefreshToken 삭제
            String refreshToken  = null;
            try {
                refreshToken = CookieUtil.getRefreshTokenCookie(request, tokenProperties.getAuth().getRefreshTokenName());
            } catch (NullPointerException e) {
                log.info("리프래쉬 토큰이 없음");
            }
            log.info("리프래쉬 토큰 가져오기 : {}", refreshToken);
            AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);
            // 리프래쉬 토큰이 있으면 redis 에서 삭제하고 쿠키에서 삭제하기
            if(refreshToken != null) {
                String userId = tokenProvider.getClaims(authRefreshToken).getSubject();
                log.info("Log out id = {}", userId);
                redisUtil.delData(userId);
                // 쿠키에서 삭제하기
                CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
            }

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            final ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.writeValue(response.getOutputStream(), CommonResponse.success("로그아웃 성공"));
            response.setStatus(HttpServletResponse.SC_OK);

//        response.sendRedirect("/");
        } catch (Exception e) {
            log.info("JwtExceptionFilter 에서 에러 받기");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            e.printStackTrace();
            log.info("Logout ErrorResponse");
            final ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.writeValue(response.getOutputStream(), CommonResponse.fail("Logout Error발생", e.getMessage()));
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}

