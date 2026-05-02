package com.securetasker.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.securetasker.dto.AuthResponse;
import com.securetasker.dto.LoginRequest;
import com.securetasker.dto.RegisterRequest;
import com.securetasker.dto.UserProfileResponse;
import com.securetasker.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
    return ResponseEntity.ok(authService.register(request));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }

  @GetMapping("/me")
  public ResponseEntity<UserProfileResponse> profile() {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity.ok(authService.getProfile(email));
  }
}
