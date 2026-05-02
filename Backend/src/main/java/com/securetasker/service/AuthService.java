package com.securetasker.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.securetasker.dto.AuthResponse;
import com.securetasker.dto.LoginRequest;
import com.securetasker.dto.RegisterRequest;
import com.securetasker.entity.Role;
import com.securetasker.entity.User;
import com.securetasker.exception.BadRequestException;
import com.securetasker.repository.UserRepository;
import com.securetasker.security.JwtUtil;
import com.securetasker.dto.UserProfileResponse;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
  }

  public AuthResponse register(RegisterRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new BadRequestException("Email already registered");
    }

    User user = new User();
    user.setName(request.getName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(Role.ROLE_USER);

    userRepository.save(user);

    String token = jwtUtil.generateToken(user.getEmail());
    return new AuthResponse(token, user.getRole().name(), user.getName(), user.getEmail());
  }

  public AuthResponse login(LoginRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new BadRequestException("Invalid credentials"));

    String token = jwtUtil.generateToken(user.getEmail());
    return new AuthResponse(token, user.getRole().name(), user.getName(), user.getEmail());
  }

  public UserProfileResponse getProfile(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new BadRequestException("User not found"));
    return new UserProfileResponse(user.getName(), user.getEmail(), user.getRole().name());
  }
}
