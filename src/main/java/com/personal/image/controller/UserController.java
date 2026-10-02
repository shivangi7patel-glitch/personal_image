package com.personal.image.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
@GetMapping("/me")
public String getCurrentUser(@AuthenticationPrincipal Jwt jwt) {

    if (jwt == null) {
        return "No JWT received";
    }

    return "Logged in as: " + jwt.getSubject();
}
}