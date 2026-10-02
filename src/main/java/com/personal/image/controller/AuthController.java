package com.personal.image.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.personal.image.dto.LoginRequest;
import com.personal.image.dto.LoginResponse;
import com.personal.image.dto.RegisterRequest;
import com.personal.image.dto.UserResponse;
import com.personal.image.entity.User;
import com.personal.image.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

   @PostMapping("/register")
public ResponseEntity<UserResponse> register(
        @RequestBody RegisterRequest request) {

    User user = authService.register(request);

    UserResponse response = new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail()
    );

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
}

@PostMapping("/login")
public ResponseEntity<LoginResponse> login(
        @RequestBody LoginRequest request) {

    LoginResponse response = authService.login(request);

    return ResponseEntity.ok(response);
}
}