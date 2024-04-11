package com.whiteasher.simpleboard.board.entity;


import com.whiteasher.simpleboard.auth.entity.User;
import com.whiteasher.simpleboard.common.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;
    private String title;
    private String content;
    @OneToOne
    @JoinColumn(name = "user_no")
    private User user;

    @Builder
    public Board(Long no, String title, String content, User user) {
        this.no = no;
        this.title = title;
        this.content = content;
        this.user = user;
    }
}
