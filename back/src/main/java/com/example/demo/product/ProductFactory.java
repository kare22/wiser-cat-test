package com.example.demo.product;

import com.example.demo.product.category.ProductCategory;
import com.example.demo.product.category.ProductCategoryRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Component
public class ProductFactory {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    private final Random random = new Random();

    public ProductFactory(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    public void create() {
        Faker faker = new Faker();

        Product product = new Product();
        product.setName(faker.commerce().productName());
        product.setPrice(generatePrice());
        product.setCategories(getRandomCategories());

        productRepository.save(product);
    }

    private double generatePrice() {
        return 10 + (random.nextDouble() * 90);
    }

    private Set<ProductCategory> getRandomCategories() {
        List<ProductCategory> allCategories = productCategoryRepository.findAll();
        Set<ProductCategory> selectedCategories = new HashSet<>();

        int numCategoriesToAdd = 1 + random.nextInt(Math.min(5, allCategories.size()));

        for (int i = 0; i < numCategoriesToAdd; i++) {
            int randomIndex = random.nextInt(allCategories.size());
            selectedCategories.add(allCategories.get(randomIndex));
        }

        return selectedCategories;
    }
}
