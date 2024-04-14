package com.whiteasher.simpleboard.auth.dto;

import com.whiteasher.simpleboard.auth.entity.ProviderType;
import com.whiteasher.simpleboard.auth.entity.RoleType;
import com.whiteasher.simpleboard.auth.entity.UserPrincipal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class UserDto {

    @Getter
    @Setter
    @ToString
    public static class Request {
        private Long no;
        private String id;
        private String password;
        private String email;
        private String nickname;
        private ProviderType providerType;

        public static UserPrincipal toUserPrincipal(UserDto.Request request) {
            return UserPrincipal.builder()
                    .id(request.getId())
                    .password(request.getPassword())
                    .email(request.getEmail())
                    .nickname(request.getNickname())
                    .providerType(request.getProviderType())
                    .build();
        }
    }

    @Getter
    @Setter
    @ToString
    public static class Response {
        private Long no;
        private String id;
        private String password;
        private String email;
        private String nickname;
        private ProviderType providerType;
        private RoleType roleType;

        public Response (UserPrincipal userPrincipal) {
            this.no = userPrincipal.getNo();
            this.id = userPrincipal.getId();
            this.password = userPrincipal.getPassword();
            this.email = userPrincipal.getEmail();
            this.nickname = userPrincipal.getNickname();
            this.providerType = userPrincipal.getProviderType();
            this.roleType = userPrincipal.getRoleType();
        }
    }

    @Getter
    @ToString
    public static class OauthUserResponse {
        private final Long no;
        private final String email;
        private final String nickname;
        private final String providerType;

        public OauthUserResponse(UserPrincipal userPrincipal) {
            this.no = userPrincipal.getNo();
            this.email = userPrincipal.getEmail();
            this.nickname = userPrincipal.getNickname();
            this.providerType = userPrincipal.getProviderType().name();
        }
    }

}
