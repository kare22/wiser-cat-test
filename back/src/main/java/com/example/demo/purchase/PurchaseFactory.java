package com.example.demo.purchase;

import com.example.demo.filter.*;
import com.example.demo.product.Product;
import com.example.demo.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
public class PurchaseFactory {

    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;
    private final FilterGroupRepository filterGroupRepository;

    private final FilterRepository filterRepository;

    @Autowired
    public PurchaseFactory(ProductRepository productRepository, PurchaseRepository purchaseRepository, FilterGroupRepository filterGroupRepository, FilterRepository filterRepository) {
        this.productRepository = productRepository;
        this.purchaseRepository = purchaseRepository;
        this.filterGroupRepository = filterGroupRepository;
        this.filterRepository = filterRepository;
    }

    public void create() {
        List<Product> availableProducts = productRepository.findAll();

        if (availableProducts.isEmpty()) {
            return;
        }

        int randomIndex = new Random().nextInt(availableProducts.size());

        Product selectedProduct = availableProducts.get(randomIndex);

        int randomAmount = new Random().nextInt(10) + 1;

        Purchase purchase = new Purchase();
        purchase.setProduct(selectedProduct);
        purchase.setAmount(randomAmount);

        Random dateRandom = new Random();
        long maxTime = System.currentTimeMillis();
        long minTime = maxTime - (1000L * 60 * 60 * 24 * 30);
        long randomTime = (long) (dateRandom.nextDouble() * (maxTime - minTime)) + minTime;
        purchase.setCreatedAt(new Date(randomTime));

        purchaseRepository.save(purchase);
    }

    public void createFilter1() {
        FilterGroup filterGroup = new FilterGroup();
        filterGroup.setName("First filter");
        filterGroup.setClassName("Purchase");
        filterGroup.setActive(true);

        filterGroupRepository.save(filterGroup);

        Filter dateFilter = new Filter("createdAt", FilterOperator.DATE_FROM, "01.10.23", filterGroup);
        Filter categoryFilter = new Filter("category", FilterOperator.HAS, "1", filterGroup);

        filterRepository.save(dateFilter);
        filterRepository.save(categoryFilter);
    }

    public void createFilter2() {
        FilterGroup filterGroup = new FilterGroup();
        filterGroup.setName("Second filter");
        filterGroup.setClassName("Purchase");
        filterGroup.setActive(true);

        filterGroupRepository.save(filterGroup);

        Filter nameFilter = new Filter("name", FilterOperator.CONTAINS, "ha", filterGroup);

        filterRepository.save(nameFilter);
    }

    public void createFilter3() {
        FilterGroup filterGroup = new FilterGroup();
        filterGroup.setName("Third filter");
        filterGroup.setClassName("Purchase");
        filterGroup.setActive(true);

        filterGroupRepository.save(filterGroup);

        Filter amountFilter = new Filter("amount", FilterOperator.GREATER_THAN, "3", filterGroup);

        filterRepository.save(amountFilter);
    }
}
