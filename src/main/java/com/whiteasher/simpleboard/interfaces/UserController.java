package com.whiteasher.simpleboard.interfaces;

import com.whiteasher.simpleboard.auth.dto.UserDto;
import com.whiteasher.simpleboard.auth.entity.UserPrincipal;
import com.whiteasher.simpleboard.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@RestController
@Slf4j
public class UserController {

    private final UserService userService;

    // TODO : 나중에 token 붙여서 요청하도록 만들기
    @GetMapping
    @Operation(summary = "회원 정보 조회")
    public ResponseEntity<?> getUser(@RequestParam(name = "no") Long no) {
        UserPrincipal result = userService.retrieveUser(no);
        return ResponseEntity.ok(new UserDto.Response(result));
    }

    @GetMapping("/list")
    @Operation(summary = "모든 회원 정보 조회")
    public ResponseEntity<?> getAllUser(@RequestParam(name = "no") Long no) {
        UserPrincipal result = userService.retrieveUser(no);
        return ResponseEntity.ok(new UserDto.Response(result));
    }

    @PostMapping("/register")
    @Operation(summary = "회원 정보 등록")
    public ResponseEntity<?> insertUser(@RequestBody UserDto.Request request) {
        UserPrincipal result = userService.insertUser(request);
        return ResponseEntity.ok(new UserDto.Response(result));
    }

    @PatchMapping
    @Operation(summary = "회원 정보 수정")
    public ResponseEntity<?> updateUser(@RequestBody UserDto.Request request) {
        UserPrincipal result = userService.insertUser(request);
        return ResponseEntity.ok(new UserDto.Response(result));
    }
}
