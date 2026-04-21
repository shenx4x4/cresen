package com.cresen.controller;

import com.cresen.model.Product;
import com.cresen.model.User;
import com.cresen.repository.ProductRepository;
import com.cresen.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shop")
@CrossOrigin(origins = "*") // Agar frontend di port 5500 bisa akses
public class ShopController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ShopController(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    // 1. Ambil semua daftar produk
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    // 2. Fungsi Top Up Saldo
    @PostMapping("/topup")
    public ResponseEntity<?> topUp(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        Double amount = Double.valueOf(request.get("amount").toString());

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
            "message", "Top up berhasil",
            "newBalance", user.getBalance()
        ));
    }

    // 3. Fungsi Beli Barang (Transactional)
    @PostMapping("/buy")
    @Transactional
    public ResponseEntity<?> buyProduct(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        Long productId = Long.valueOf(request.get("productId").toString());

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan"));

        // Validasi Saldo
        if (user.getBalance() < product.getPrice()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Saldo kamu tidak cukup!"));
        }

        // Validasi Stok
        if (product.getStock() <= 0) {
            return ResponseEntity.badRequest().body(Map.of("message", "Stok barang sudah habis!"));
        }

        // Proses Transaksi
        user.setBalance(user.getBalance() - product.getPrice());
        product.setStock(product.getStock() - 1);

        // Simpan perubahan
        userRepository.save(user);
        productRepository.save(product);

        return ResponseEntity.ok(Map.of(
            "message", "Berhasil membeli " + product.getName(),
            "remainingBalance", user.getBalance()
        ));
    }
}
