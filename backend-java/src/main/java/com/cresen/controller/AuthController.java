package com.cresen.controller;

import com.cresen.model.User;
import com.cresen.repository.UserRepository;
import com.cresen.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*") // Izinkan semua akses untuk testing
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
    public ResponseEntity<?> register(@RequestBody User user) {
        System.out.println(">>> Menerima Request Register untuk user: " + user.getUsername());
        
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username sudah terpakai!"));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setBalance(0.0); // Set saldo awal
        userRepository.save(user);

        System.out.println(">>> User " + user.getUsername() + " berhasil didaftarkan!");
        return ResponseEntity.ok(Map.of("message", "Registrasi berhasil!"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        System.out.println(">>> Attempt Login untuk user: " + username);

        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            String token = jwtService.generateToken(username);
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", username);
            response.put("message", "Login Berhasil");

            System.out.println(">>> Login SUKSES untuk user: " + username);
            return ResponseEntity.ok(response);
        }

        System.out.println(">>> Login GAGAL untuk user: " + username);
        return ResponseEntity.status(401).body(Map.of("message", "Username atau Password salah!"));
    }
}
