package com.lifeshield.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AppUserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public AuthController(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // USER REGISTRATION
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest) {

        if (request.name() == null ||
            request.email() == null ||
            request.password() == null) {

            return ResponseEntity.badRequest()
                    .body(Map.of("message", "All fields are required"));
        }

        String name = request.name().trim();
        String email = request.email().trim().toLowerCase();
        String password = request.password();

        if (name.isBlank() || email.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Name and email cannot be empty"));
        }

        if (password.length() < 8 || password.length() > 72) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message",
                            "Password must contain 8 to 72 characters"));
        }

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Email already registered"));
        }

        AppUser user = new AppUser();

        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));

        AppUser savedUser = userRepository.save(user);

        // Create login session after registration
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute("userId", savedUser.getId());
        session.setAttribute("userName", savedUser.getName());
        session.setAttribute("userEmail", savedUser.getEmail());
        session.setMaxInactiveInterval(30 * 60);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", "Registration successful",
                        "userId", savedUser.getId(),
                        "name", savedUser.getName(),
                        "email", savedUser.getEmail()
                ));
    }

    // USER LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {

        if (request.email() == null ||
            request.password() == null) {

            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email and password are required"));
        }

        String email = request.email().trim().toLowerCase();

        Optional<AppUser> optionalUser =
                userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
        }

        AppUser user = optionalUser.get();

        if (!passwordEncoder.matches(
                request.password(), user.getPasswordHash())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
        }

        // Prevent session fixation
        HttpSession oldSession = httpRequest.getSession(false);

        if (oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession session = httpRequest.getSession(true);

        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getName());
        session.setAttribute("userEmail", user.getEmail());
        session.setMaxInactiveInterval(30 * 60);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Login successful",
                        "userId", user.getId(),
                        "name", user.getName(),
                        "email", user.getEmail()
                )
        );
    }

    // CHECK CURRENT LOGIN SESSION
    @GetMapping("/me")
    public ResponseEntity<?> currentUser(
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("userId") == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Not logged in"));
        }

        return ResponseEntity.ok(
                Map.of(
                        "userId", session.getAttribute("userId"),
                        "name", session.getAttribute("userName"),
                        "email", session.getAttribute("userEmail")
                )
        );
    }

    // USER LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok(
                Map.of("message", "Logout successful")
        );
    }

    // REQUEST DATA STRUCTURES
    public record RegisterRequest(
            String name,
            String email,
            String password
    ) {}

    public record LoginRequest(
            String email,
            String password
    ) {}
}