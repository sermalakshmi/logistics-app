package com.transport.app.controller;

import com.transport.app.dto.LoginRequestDTO;
import com.transport.app.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/*
 POST /api/auth/login  → Admin login, returns JWT token
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    // ─── POST /api/auth/login
    /*
     Admin login endpoint.
     Request:  { "username": "admin", "password": "admin123" }
     Response: { "token": "eyJ...", "username": "admin", "fullName": "..." }
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @Valid @RequestBody LoginRequestDTO loginRequest) {

        Map<String, Object> response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}