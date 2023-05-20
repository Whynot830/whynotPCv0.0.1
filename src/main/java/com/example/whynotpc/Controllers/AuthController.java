package com.example.whynotpc.Controllers;

import com.example.whynotpc.Records.AuthResponse;
import com.example.whynotpc.Records.LoginRequest;
import com.example.whynotpc.Records.RegisterRequest;
import com.example.whynotpc.Services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/profile")
    public ResponseEntity<AuthResponse> getInfo(Authentication authentication) {
        var username = authentication.getName();
        return ResponseEntity.ok(authService.getInfo(username));
    }
}
