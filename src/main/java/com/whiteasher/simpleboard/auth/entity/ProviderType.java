package com.whiteasher.simpleboard.auth.entity;

import lombok.Getter;

@Getter
public enum ProviderType {
    GOOGLE,
    FACEBOOK,
    NAVER,
    KAKAO,
    LOCAL;
}