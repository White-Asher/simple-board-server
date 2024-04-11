package com.whiteasher.simpleboard.auth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
public class AuthTokenProvider {

    private final Key key;
    private static final String AUTHORITIES_KEY = "role";

    public AuthTokenProvider(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public AuthToken createAuthToken(String id, Date expiry) {
        return new AuthToken(id, expiry, key);
    }

    public AuthToken createAuthToken(String id, String role, Date expiry) {
        return new AuthToken(id, role, expiry, key);
    }

    public AuthToken convertAuthToken(String token) {
        return new AuthToken(token, key);
    }

    public String getPayload(String token, String className) {
        return Jwts.parserBuilder().setSigningKey(key)
                        .build().parseClaimsJws(token).getBody().get(className,String.class);
    }

    // `AuthToken` 객체를 받아 `authToken.validate()` 메소드를 호출하여 `AuthToken` 객체가 유효한지 검증.
    public Authentication getAuthentication(AuthToken authToken) {
        log.info("getAuthentication CALL");
        Claims claims = authToken.getTokenClaims();
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{claims.get(AUTHORITIES_KEY).toString()})
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // User principal = new User(claims.getSubject(), "", authorities);
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        log.info("principal : {}", principal);

        return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
    }

    public Claims getClaims (AuthToken authToken){
        return authToken.getTokenClaims();
    }

}
