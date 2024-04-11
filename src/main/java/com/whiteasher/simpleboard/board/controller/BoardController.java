package com.whiteasher.simpleboard.board.controller;

import com.whiteasher.simpleboard.auth.service.UserService;
import com.whiteasher.simpleboard.board.dto.BoardDto;
import com.whiteasher.simpleboard.board.service.BoardService;
import com.whiteasher.simpleboard.config.OpenApiConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    private final UserService userService;

    @GetMapping("/retrieve")
    @Operation(summary = "게시판 글 출력")
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    public ResponseEntity<?> retrieveBoard(@RequestParam(name = "no") Long no) {
        var userPrincipal = userService.currentLoadUserByUserId();
        return ResponseEntity.ok(boardService.retrieveBoardByNo(no, userPrincipal));
    }

    @PostMapping()
    @Operation(summary = "게시판 글 등록")
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    public ResponseEntity<?> insertBoard(@RequestBody BoardDto.Request request) {
        var userPrincipal = userService.currentLoadUserByUserId();
        return ResponseEntity.ok(boardService.insertBoard(request, userPrincipal));
    }

    @DeleteMapping()
    @Operation(summary = "게시판 글 삭제")
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    public ResponseEntity<?> deleteBoardByNo(@RequestParam(name = "no") Long no) {
        var userPrincipal = userService.currentLoadUserByUserId();
        boardService.deleteBoard(no, userPrincipal);
        return ResponseEntity.ok(null);
    }

}
