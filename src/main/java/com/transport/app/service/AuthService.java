package com.transport.app.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.transport.app.config.JwtUtil;
import com.transport.app.dto.LoginRequestDTO;
import com.transport.app.exception.CustomException;
import com.transport.app.model.Admin;
import com.transport.app.repository.AdminRepository;


@Service
public class AuthService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // ─── LOGIN 
    /**
     * Validates admin credentials and returns a JWT token on success.
     * Returns a map with token + admin info for the frontend.
     */
    public Map<String, Object> login(LoginRequestDTO loginRequest) {
        // 1. Find admin by username
        Admin admin = adminRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new CustomException(
                    "Invalid username or password",
                    HttpStatus.UNAUTHORIZED
                ));

        // 2. Verify password against BCrypt hash
        if (!passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
            throw new CustomException("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }

        // 3. Generate JWT token
        String token = jwtUtil.generateToken(admin.getUsername());

        // 4. Build response
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", admin.getUsername());
        response.put("fullName", admin.getFullName());
        response.put("message", "Login successful");

        return response;
    }
}