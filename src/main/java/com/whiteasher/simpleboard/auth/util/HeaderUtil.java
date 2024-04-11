package com.whiteasher.simpleboard.auth.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HeaderUtil {

    public static String getAccessToken(HttpServletRequest request, String headerAuth, String tokenPrefix) {
        String headerValue = request.getHeader(headerAuth);
        log.info("Access Token (HEADER) = {}", headerValue);
        if (headerValue == null) {
            return null;
        }
        if (headerValue.startsWith(tokenPrefix)) {
            return headerValue.substring(tokenPrefix.length());
        }
        return null;
    }
}