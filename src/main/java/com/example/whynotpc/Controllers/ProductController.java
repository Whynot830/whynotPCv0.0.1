package com.example.whynotpc.Controllers;

import com.example.whynotpc.Models.Product;
import com.example.whynotpc.Services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping(consumes = ("application/json"))
    public ResponseEntity<Void> createEntity(@RequestBody Product product) {
        productService.create(product);
        return ResponseEntity.created(null).build();
    }

    @PostMapping(value = "/all", consumes = ("application/json"))
    public ResponseEntity<Void> createEntities(@RequestBody List<Product> products) {

        productService.create(products);
        return ResponseEntity.created(null).build();
    }

    @GetMapping
    public ResponseEntity<List<Product>> readAll() {
        return ResponseEntity.ok(productService.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> read(@PathVariable("id") Integer id) {
        Optional<Product> optionalEntity = productService.readOne(id);
        return optionalEntity.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Product product) {
        if (productService.update(product, id))
            return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        if (productService.delete(id))
            return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }
}
