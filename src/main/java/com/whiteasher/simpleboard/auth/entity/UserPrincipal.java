package com.whiteasher.simpleboard.auth.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
@Builder
@ToString
@Slf4j
@RequiredArgsConstructor
public class UserPrincipal implements OAuth2User, UserDetails, OidcUser {
    private final Long no;
    private final String id;
    private final String password;
    private final String email;
    private final String nickname;
    private final ProviderType providerType;
    private final RoleType roleType;
    private final Collection<GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    @Builder
    public UserPrincipal(Long no, String id, String password, String email, String nickname, ProviderType providerType, RoleType roleType, Collection<GrantedAuthority> authorities, Map<String, Object> attributes) {
        this.no = no;
        this.id = id;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.providerType = providerType;
        this.roleType = roleType;
        this.authorities = authorities;
        this.attributes = attributes;
    }

    public static User toEntity(UserPrincipal userPrincipal) {
        return User.builder()
                .no(userPrincipal.getNo())
                .id(userPrincipal.getId())
                .password(userPrincipal.getPassword())
                .email(userPrincipal.getEmail())
                .nickname(userPrincipal.getNickname())
                .roleType(userPrincipal.getRoleType())
                .providerType(userPrincipal.getProviderType())
                .build();
    }


    public static UserPrincipal toOauthUserPrincipal(User user) {
        log.info("toOauthUserPrincipal : {}" , user);
        log.info("toOauthUserPrincipal getrole: {}" , user.getRoleType());
        SimpleGrantedAuthority simpleGrantedAuthority;
        if (user.getRoleType().getCode().equals("ROLE_ADMIN")) {
            simpleGrantedAuthority = new SimpleGrantedAuthority(RoleType.ADMIN.getCode());
        } else {
            simpleGrantedAuthority = new SimpleGrantedAuthority(RoleType.USER.getCode());
        }
        return UserPrincipal.builder()
                .no(user.getNo())
                .id(user.getId())
                .password(user.getPassword())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .roleType(user.getRoleType())
                .providerType(user.getProviderType())
                .authorities(Collections.singleton(simpleGrantedAuthority))
                .build();
    }

    public static UserPrincipal toOauthUserPrincipal(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = toOauthUserPrincipal(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    private void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // below code not use
    @Override
    public String getName() {
        return nickname;
    }

    @Override
    public String getUsername() {
        return nickname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getClaims() {
        return null;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }

}

