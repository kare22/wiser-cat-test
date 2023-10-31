package com.example.demo.purchase;

import com.example.demo.filter.*;
import com.example.demo.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.product.Product;

import java.util.List;

@RestController
@RequestMapping("/purchases")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PurchaseController extends FilterController {

    private final PurchaseService purchaseService;
    private final PurchaseRepository repository;
    private final ProductRepository productRepository;

    @Autowired
    public PurchaseController(PurchaseService purchaseService, PurchaseRepository repository, ProductRepository productRepository) {
        this.purchaseService = purchaseService;
        this.repository = repository;
        this.productRepository = productRepository;
    }

    @GetMapping()
    public ResponseEntity<?> getPurchases() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredPurchases() {
        return ResponseEntity.ok(purchaseService.getPurchasesWithFilters());
    }

    @PostMapping("/{productId}")
    public ResponseEntity<String> addPurchase(
            @PathVariable Long productId,
            @RequestBody Purchase purchase) {
        try {
            Product product = productRepository.findById(productId).orElse(null);
            if (product == null) {
                return ResponseEntity.notFound().build();
            }

            purchase.setProduct(product);
            repository.save(purchase);

            return ResponseEntity.ok("Purchase added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding purchase");
        }
    }

    @Override
    protected String getClassName() {
        return "Purchase";
    }
}
