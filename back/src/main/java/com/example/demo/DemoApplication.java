package com.example.demo;

import com.example.demo.product.ProductFactory;
import com.example.demo.product.category.ProductCategoryFactory;
import com.example.demo.purchase.PurchaseFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class DemoApplication {
	private final ProductCategoryFactory productCategoryFactory;
	private final PurchaseFactory purchaseFactory;
	private final ProductFactory productFactory;

	public DemoApplication(ProductCategoryFactory productCategoryFactory, PurchaseFactory purchaseFactory, ProductFactory productFactory) {
		this.purchaseFactory = purchaseFactory;
		this.productFactory = productFactory;
		this.productCategoryFactory = productCategoryFactory;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void runAfterStartup() {
		for (int i = 0; i < 4; i++) {
			productCategoryFactory.create();
		}
		for (int i = 0; i < 1000; i++) {
			productFactory.create();
		}
		for (int i = 0; i < 1000; i++) {
			purchaseFactory.create();
		}

		purchaseFactory.createFilter1();
		purchaseFactory.createFilter2();
		purchaseFactory.createFilter3();
	}

	public static void main(String[] args)
	{
		SpringApplication.run(DemoApplication.class, args);
	}
}