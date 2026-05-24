package com.sms.society_management.controller;

import com.sms.society_management.dto.request.AuthRequest;
import com.sms.society_management.dto.response.AuthResponse;
import com.sms.society_management.exception.TooManyRequestsException;
import com.sms.society_management.security.AdminUserDetailsService;
import com.sms.society_management.security.JwtUtil;
import com.sms.society_management.security.LoginRateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AdminUserDetailsService adminUserDetailsService;
    private final JwtUtil jwtUtil;
    private final LoginRateLimiter loginRateLimiter;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody AuthRequest request,
            HttpServletRequest httpRequest) {

        // Rate-limit by IP — 5 attempts per minute
        String clientIp = getClientIp(httpRequest);
        if (!loginRateLimiter.tryConsume(clientIp)) {
            throw new TooManyRequestsException(
                    "Too many login attempts. Please wait 1 minute and try again.");
        }

        log.info("Login attempt for username: {} from IP: {}", request.getUsername(), clientIp);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()));

        UserDetails userDetails = adminUserDetailsService
                .loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        log.info("Login successful for username: {}", request.getUsername());
        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .expiresIn(jwtUtil.getExpirationMs())
                .build());
    }

    // ----------------------------------------------------------------
    // Extract real IP (handles reverse proxy via X-Forwarded-For)
    // ----------------------------------------------------------------
    private String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim(); // first IP in chain = original client
        }
        return request.getRemoteAddr();
    }
}
