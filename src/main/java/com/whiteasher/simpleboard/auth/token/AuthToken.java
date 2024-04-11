package com.whiteasher.simpleboard.auth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;

/**
 * JWT 토큰을 생성하고 검증하는데 사용되는 `AuthToken` 클래스. <br>
 */
@Slf4j
@ToString
@RequiredArgsConstructor
public class AuthToken {
    @Getter
    private final String token;
    private final Key key;
    private static final String AUTHORITIES_KEY = "role";

    AuthToken(String id, Date expiry, Key key) {
        this.key = key;
        this.token = createAuthToken(id, expiry);
    }

    AuthToken(String id, String role, Date expiry, Key key) {
        this.key = key;
        this.token = createAuthToken(id, role, expiry);
    }

    private String createAuthToken(String id, Date expiry) {
        return Jwts.builder()
                .setSubject(id)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact();
    }

    private String createAuthToken(String id, String role, Date expiry) {
        return Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, role)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact();
    }

    public boolean validate() {
        // 토큰이 정상이면 토큰값이 리턴되므로 null 이 아님, ture 리턴됨
        return this.getTokenClaims() != null;
    }

    /**
     * UnsupportedJwtException : 지원되지 않는 형식이거나 구성의 JWT 토큰 <br>
     * MalformedJwtException : 유효하지 않은 구성의 JWT 토큰 <br>
     * ExpiredJwtException : 만료된 JWT 토큰 <br>
     * SignatureException : 잘못된 JWT 서명 <br>
     * IllegalArgumentException : 잘못된 JWT <br>
     */
    public Claims getTokenClaims() {
        log.info("getTokenClaims 메서드 호출");
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (MalformedJwtException e) {
            log.warn("MalformedJwtException JWT token.");
            throw new MalformedJwtException("잘못된 형식의 JWT 토큰");
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token.");
            throw new ExpiredJwtException(Jwts.header(), Jwts.claims(), "JWT 토큰 만료");
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token.");
            throw new UnsupportedJwtException("지원하지 않는 방식의 JWT 토큰");
        } catch (IllegalArgumentException e) {
            log.warn("JWT token compact of handler are invalid.");
            throw new IllegalArgumentException("핸들러의 JWT 토큰 압축이 잘못됨");
        } catch (JwtException e) {
            log.warn("invalid JWT TOKEN");
            throw new JwtException("토큰이 위변조 되었음");
        } catch (SecurityException e) {
            log.warn("SecurityException");
            throw new SecurityException("Security 에러");
        }
    }

}
