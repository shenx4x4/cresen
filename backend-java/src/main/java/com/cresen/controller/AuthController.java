package com.cresen.controller;

import com.cresen.model.User;
import com.cresen.repository.UserRepository;
import com.cresen.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Penting agar frontend bisa akses
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        // Validasi jika user sudah ada
        if (userRepository.findByUsername(request.get("username")).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username sudah terdaftar!"));
        }

        User user = new User();
        user.setUsername(request.get("username"));
        user.setEmail(request.get("email"));
        // Password di-hash dengan BCrypt sebelum masuk DB
        user.setPassword(passwordEncoder.encode(request.get("password")));
        user.setRole("ROLE_USER");
        
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Registrasi Berhasil"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        Optional<User> userOpt = userRepository.findByUsername(request.get("username"));
        
        if (userOpt.isPresent() && passwordEncoder.matches(request.get("password"), userOpt.get().getPassword())) {
            String token = jwtService.generateToken(userOpt.get().getUsername());
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.status(401).body(Map.of("message", "Username atau Password salah"));
    }
}
