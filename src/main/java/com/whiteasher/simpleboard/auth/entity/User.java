package com.whiteasher.simpleboard.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicUpdate;

@Slf4j
@Getter
@Entity
@NoArgsConstructor
@DynamicUpdate
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;
    private String id;
    private String password;
    private String email;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Builder
    public User(
            Long no,
            String id,
            String password,
            String email,
            String nickname,
            RoleType roleType,
            ProviderType providerType
    ) {
        // if (StringUtils.isEmpty(email)) throw new RuntimeException("empty userEmail");
        // if (StringUtils.isEmpty(nickname)) throw new RuntimeException("empty userNickname");

        this.no = no;
        this.id = id;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.roleType = roleType;
        this.providerType = providerType;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

}
