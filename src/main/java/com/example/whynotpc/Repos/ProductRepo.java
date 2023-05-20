package com.example.whynotpc.Repos;

import com.example.whynotpc.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Integer> {
}
