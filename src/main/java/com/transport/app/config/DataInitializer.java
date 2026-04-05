package com.transport.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.transport.app.model.Admin;
import com.transport.app.repository.AdminRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!adminRepository.existsByUsername("admin")) {
            Admin defaultAdmin = new Admin(
                "admin",
                passwordEncoder.encode("admin123"),
                "Business Owner"
            );
            adminRepository.save(defaultAdmin);
            System.out.println("✅ Default admin created → username: admin | password: admin123");
        } else {
            System.out.println("✅ Admin already exists. Ready.");
        }
    }
}