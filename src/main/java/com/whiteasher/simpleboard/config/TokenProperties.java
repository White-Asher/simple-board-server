package com.whiteasher.simpleboard.config;

import com.whiteasher.simpleboard.auth.token.AuthTokenProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Getter
@Configuration
@ConfigurationProperties(prefix = "token")
public class TokenProperties {

    private final Auth auth = new Auth();
    private final OAuth2 oauth2 = new OAuth2();

    @Bean
    public AuthTokenProvider jwtProvider() {
        return new AuthTokenProvider(getAuth().getTokenSecret());
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Auth {
        private String tokenSecret;
        private long accessTokenExpiry;
        private long refreshTokenExpiry;
        private String accessTokenHeaderName;
        private String accessTokenHeaderPrefix;
        private String refreshTokenName;
    }

    @Getter
    public static final class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();

        public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
            this.authorizedRedirectUris = authorizedRedirectUris;
            return this;
        }
    }

}
