package com.whiteasher.simpleboard.board.dto;

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

        public Board toEntity() {
            return Board.builder()
                    .no(this.no)
                    .title(this.title)
                    .content(this.content)
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

        public Response(Board board) {
            this.no = board.getNo();
            this.title = board.getTitle();
            this.content = board.getContent();
        }
    }
}
