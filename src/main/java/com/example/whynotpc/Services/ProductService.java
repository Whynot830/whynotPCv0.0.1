package com.example.whynotpc.Services;

import com.example.whynotpc.Models.Product;
import com.example.whynotpc.Repos.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;

    public void create(Product product) {
        productRepo.save(product);
    }

    public void create(List<Product> products) {
        productRepo.saveAll(products);
    }


    public List<Product> readAll() {
        return productRepo.findAll();
    }

    public Optional<Product> readOne(Integer id) {
        return productRepo.findById(id);
    }

    public boolean update(Product newEntity, Integer id) {
        Optional<Product> optionalEntity = productRepo.findById(id);
        if (optionalEntity.isEmpty())
            return false;
        newEntity.setId(id);
        productRepo.save(newEntity);
        return true;
    }

    public boolean delete(Integer id) {
        Optional<Product> optionalEntity = productRepo.findById(id);
        if (optionalEntity.isEmpty())
            return false;
        productRepo.delete(optionalEntity.get());
        return true;
    }
}
