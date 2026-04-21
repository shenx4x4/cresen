package com.cresen.repository;

import com.cresen.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Otomatis mendapatkan fungsi findById, save, dll.
}

