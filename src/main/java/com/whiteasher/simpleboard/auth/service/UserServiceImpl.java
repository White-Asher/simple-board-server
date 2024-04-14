package com.whiteasher.simpleboard.auth.service;

import com.whiteasher.simpleboard.auth.dto.UserDto;
import com.whiteasher.simpleboard.auth.entity.ProviderType;
import com.whiteasher.simpleboard.auth.entity.RoleType;
import com.whiteasher.simpleboard.auth.entity.User;
import com.whiteasher.simpleboard.auth.entity.UserPrincipal;
import com.whiteasher.simpleboard.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserPrincipal retrieveUser(Long userNo) {
        Optional<User> result = userRepository.findByNo(userNo);
        return UserPrincipal.toOauthUserPrincipal(result.get());
    }

    @Transactional
    public UserPrincipal currentLoadUserByUserId() throws UsernameNotFoundException {
        log.info("UserServiceImpl :: currentLoadUserByUserId");
        UserDto.OauthUserResponse oauthUserResponse = null;
        org.springframework.security.core.userdetails.User principal = null;
        try {
            principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException e){
            throw new RuntimeException("currentLoadUserByUserId에서 유저 정보를 가져올 수 없음");
        }
        String id = principal.getUsername();
        User retrieve = userRepository.findById(id).orElseThrow();
        return UserPrincipal.toOauthUserPrincipal(retrieve);
    }

    @Override
    @Transactional
    public UserPrincipal updateUserNickname(String nickname) {
        UserPrincipal userPrincipal = currentLoadUserByUserId();
        User getUser = userRepository.findById(userPrincipal.getId()).orElseThrow();
        getUser.updateNickname(nickname);
        return UserPrincipal.toOauthUserPrincipal(getUser);
    }

    @Override
    public UserPrincipal insertUser(UserDto.Request request) {
        UserPrincipal userPrincipal = UserPrincipal.builder()
                .id(request.getId())
                .password(request.getPassword())
                .roleType(RoleType.USER)
                .providerType(ProviderType.LOCAL)
                .build();
        User user = userRepository.save(UserPrincipal.toEntity(userPrincipal));
        return UserPrincipal.toOauthUserPrincipal(user);
    }
}
