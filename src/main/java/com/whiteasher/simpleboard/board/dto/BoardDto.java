package com.whiteasher.simpleboard.board.dto;

import com.whiteasher.simpleboard.auth.entity.UserPrincipal;
import com.whiteasher.simpleboard.board.entity.Board;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class BoardDto {

    @Getter
    @ToString
    @Builder
    @Schema(description = "게시글 등록 DTO")
    public static class Request {
        private Long no;
        private String title;
        private String content;
        private Long userNo;

        public Board toEntity(UserPrincipal userPrincipal) {
            return Board.builder()
                    .no(this.no)
                    .title(this.title)
                    .content(this.content)
                    .user(UserPrincipal.toEntity(userPrincipal))
                    .build();
        }
    }

    @Getter
    @ToString
    @Schema(description = "게시글 응답 DTO")
    public static class Response {
        private final Long no;
        private final String title;
        private final String content;
        private final Long userNo;

        public Response(Board board, UserPrincipal userPrincipal) {
            this.no = board.getNo();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.userNo = userPrincipal.getNo();
        }
    }
}
