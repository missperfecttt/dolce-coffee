package com.dolcecoffee.controller;

import com.dolcecoffee.model.Product; // Adjust package name if needed
import com.dolcecoffee.repository.ProductRepository; // Adjust package name if needed
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*") // 👈 Enables CORS so Vercel can fetch menu items
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // GET /api/v1/products
    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // POST /api/v1/products (For adding items via admin panel)
    @PostMapping("/products")
    public Product addProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }
}