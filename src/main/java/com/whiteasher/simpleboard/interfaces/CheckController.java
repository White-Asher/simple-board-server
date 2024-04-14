package com.whiteasher.simpleboard.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/check")
@RequiredArgsConstructor
@RestController
public class CheckController {
    @GetMapping()
    @Operation(summary = "서버 체크")
    public ResponseEntity<?> retrieveBoard() {
        return ResponseEntity.ok("SERVER ON");
    }
}
