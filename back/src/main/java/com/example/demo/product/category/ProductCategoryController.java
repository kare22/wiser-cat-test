package com.example.demo.product.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products-categories")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductCategoryController {
    @Autowired
    private ProductCategoryRepository repository;

    @GetMapping
    public ResponseEntity<?> getAllProductCategories() {
        return ResponseEntity.ok(repository.findAll());
    }

}
