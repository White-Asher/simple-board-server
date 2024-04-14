package com.whiteasher.simpleboard.auth.service;

import com.whiteasher.simpleboard.auth.dto.UserDto;
import com.whiteasher.simpleboard.auth.entity.UserPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    UserPrincipal retrieveUser(Long userNo);
    UserPrincipal currentLoadUserByUserId() throws UsernameNotFoundException;
    UserPrincipal updateUserNickname(String nickname);
    UserPrincipal insertUser(UserDto.Request request);
}
