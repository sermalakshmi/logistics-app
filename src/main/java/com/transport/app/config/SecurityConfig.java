package com.transport.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/*
	PUBLIC  — POST /api/bookings   (customers submit bookings)
	PUBLIC  — POST /api/auth/login (admin login)
	PROTECTED — GET /api/bookings    (view all bookings — admin only)
	PROTECTED — PUT /api/bookings/ (update status — admin only)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Value("${app.cors.allowed-origins:http://localhost:8080}")
    private String allowedOrigins;

    // ─── Security Filter Chain ───────────────────────────────────────────────────
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF (we use JWT, not sessions)
            .csrf(csrf -> csrf.disable())

            // Configure CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Stateless sessions (JWT)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                // PUBLIC: H2 console (dev only)
                .requestMatchers("/h2-console/**").permitAll()
                // PUBLIC: Auth endpoints
                .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                // PUBLIC: Customer booking submission
                .requestMatchers(HttpMethod.POST, "/api/bookings").permitAll()
                // PUBLIC: Serve static frontend files
                .requestMatchers("/", "/index.html", "/admin-login.html",
                                 "/dashboard.html", "/style.css", "/**.js").permitAll()
                // PROTECTED: Everything else requires admin JWT
                .anyRequest().authenticated()
            )

            // Allow H2 console frames (dev only)
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))

            // Add JWT filter before username/password filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ─── CORS Configuration
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Parse allowed origins from properties
        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        config.setAllowedOrigins(origins);
        config.addAllowedOrigin("http://127.0.0.1:5500");   // VS Code Live Server
        config.addAllowedOrigin("http://localhost:5500");
        config.addAllowedOrigin("null");                      // file:// opened locally

        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // ─── Password Encoder (BCrypt) 
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ─── Authentication Manager 
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}