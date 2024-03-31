package com.whiteasher.simpleboard.board.controller;

import com.whiteasher.simpleboard.board.dto.BoardDto;
import com.whiteasher.simpleboard.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
@RestController
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/retrieve")
    @Operation(summary = "게시판 글 출력")
    public ResponseEntity<?> retrieveBoard(@RequestParam(name = "no") Long no) {
        return ResponseEntity.ok(boardService.retrieveBoardByNo(no));
    }

    @PostMapping()
    @Operation(summary = "게시판 글 출력")
    public ResponseEntity<?> insertBoard(@RequestBody BoardDto.Request request) {
        return ResponseEntity.ok(boardService.insertBoard(request));
    }

    @DeleteMapping()
    @Operation(summary = "게시판 글 삭제")
    public ResponseEntity<?> deleteBoardByNo(@RequestParam(name = "no") Long no) {
        boardService.deleteBoard(no);
        return ResponseEntity.ok(null);
    }

}
