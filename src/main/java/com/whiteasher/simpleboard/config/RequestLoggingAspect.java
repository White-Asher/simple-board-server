package com.whiteasher.simpleboard.config;

import com.google.common.base.Joiner;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class RequestLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingAspect.class);

    private String paramMapToString(Map<String, String[]> paramMap) {
        return paramMap.entrySet().stream()
                .map(entry -> String.format("%s -> (%s)",
                        entry.getKey(), Joiner.on(",").join(entry.getValue())))
                .collect(Collectors.joining(", "));
    }

    @Pointcut("within(com.whiteasher.simpleboard.interfaces..*)")
    public void onRequest() {}

    @Around("com.whiteasher.simpleboard.config.RequestLoggingAspect.onRequest()")
    public Object doLogging(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        Map<String, String[]> paramMap = request.getParameterMap();
        String params = "";
        if (!paramMap.isEmpty()) {
            params = " [" + paramMapToString(paramMap) + "]";
        }

        long start = System.currentTimeMillis();
        try {
            return pjp.proceed(pjp.getArgs());
        } finally {
            long end = System.currentTimeMillis();
            logger.info("Request: {} {}{} | Host: {} ({}ms)", request.getMethod(), request.getRequestURI(),
                    params, request.getRemoteHost(), end - start);
        }
    }
}
