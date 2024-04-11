package com.whiteasher.simpleboard.auth.service;

import com.whiteasher.simpleboard.auth.entity.ProviderType;
import com.whiteasher.simpleboard.auth.entity.RoleType;
import com.whiteasher.simpleboard.auth.entity.User;
import com.whiteasher.simpleboard.auth.entity.UserPrincipal;
import com.whiteasher.simpleboard.auth.exception.OAuthProviderMissMatchException;
import com.whiteasher.simpleboard.auth.info.OAuth2UserInfo;
import com.whiteasher.simpleboard.auth.info.OAuth2UserInfoFactory;
import com.whiteasher.simpleboard.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CALL -> CustomOAuth2UserService : loadUser");
        OAuth2User user = super.loadUser(userRequest);
        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    public OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        log.info("CALL -> CustomOAuth2UserService : process");
        ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        log.info("LOGIN userInfo : {} ", userInfo.getAttributes());

        // 회원 정보 없으면 가입
        Optional<User> result = userRepository.findById(userInfo.getId());
        log.info("DB userInfo : {} ", result);

        User savedUser = result.orElse(null);

        if (savedUser != null) {
            // 다른 곳에서 이미 가입했을 때
            if (providerType != savedUser.getProviderType()) {
                throw new OAuthProviderMissMatchException(
                        "Looks like you're signed up with " + providerType +
                                " account. Please use your " + savedUser.getProviderType() + " account to login."
                );
            }

        } else {
            log.info("회원 정보가 없습니다. 가입을 처리합니다");
            savedUser = createUser(userInfo, providerType);
        }
        return UserPrincipal.toOauthUserPrincipal(savedUser, user.getAttributes());
    }

    // 소셜회원가입 (소셜 로그인 시 DB에 없으면 회원가입 로직을 수행함)
    public User createUser(OAuth2UserInfo userInfo, ProviderType providerType) {
        log.info("userInfo : {} ", userInfo);
        User user = User.builder()
                .id(userInfo.getId())
                .email(userInfo.getEmail() != null ? userInfo.getEmail() : "NO_EMAIL")
                .nickname(userInfo.getName() != null ? userInfo.getName() : "NO_NICKNAME")
                .providerType(providerType)
                .roleType(RoleType.USER)
                .build();
        return userRepository.saveAndFlush(user);
    }

}