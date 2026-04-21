package com.cresen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        // Memastikan server berjalan di port 8080
        SpringApplication.run(Application.class, args);
    }

    /**
     * Bean PasswordEncoder diletakkan di sini agar bisa di-Inject 
     * ke AuthController dan SecurityConfig tanpa duplikasi kode.
     * BCrypt adalah standar industri untuk hashing password yang aman.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
