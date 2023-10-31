package com.example.demo.purchase;

import com.example.demo.filter.FilterGroup;
import com.example.demo.filter.FilterGroupRepository;
import com.example.demo.product.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PurchaseService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private FilterGroupRepository filterGroupRepository;

    public List<Purchase> getPurchasesWithFilters() {
        List<FilterGroup> filterGroups = filterGroupRepository.findAll();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Purchase> cq = cb.createQuery(Purchase.class);

        Root<Purchase> purchaseRoot = cq.from(Purchase.class);
        Join<Purchase, Product> productJoin = purchaseRoot.join("product");

        List<Predicate> predicates = new ArrayList<>();

        if (!filterGroups.isEmpty()) {
            filterGroups.forEach(filterGroup -> {
                filterGroup.getFilters().forEach(filter -> {
                    switch (filter.getOperator()) {
                        case EQUALS:
                            try {
                                predicates.add(cb.equal(purchaseRoot.get(filter.getValueName()), Integer.parseInt(filter.getValue())));
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            break;
                        case GREATER_THAN:
                            try {
                                predicates.add(cb.greaterThan(purchaseRoot.get(filter.getValueName()), Integer.parseInt(filter.getValue())));
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            break;
                        case LESSER_THAN:
                            try {
                                predicates.add(cb.lessThan(purchaseRoot.get(filter.getValueName()), Integer.parseInt(filter.getValue())));
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            break;
                        case BEGINS_WITH:
                            predicates.add(cb.like(cb.lower(productJoin.get(filter.getValueName())), filter.getValue().toLowerCase() + "%"));
                            break;
                        case CONTAINS:
                            predicates.add(cb.like(cb.lower(productJoin.get(filter.getValueName())), "%" + filter.getValue().toLowerCase() + "%"));
                            break;
                        case ENDS_WITH:
                            predicates.add(cb.like(cb.lower(productJoin.get(filter.getValueName())), "%" + filter.getValue().toLowerCase()));
                            break;
                        case DATE_FROM:
                            try {
                                Date date = new SimpleDateFormat("dd.MM.yy").parse(filter.getValue());
                                predicates.add(cb.greaterThanOrEqualTo(purchaseRoot.<Date>get(filter.getValueName()), date));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            break;
                        case DATE_TO:
                            try {
                                Date date = new SimpleDateFormat("dd.MM.yy").parse(filter.getValue());
                                predicates.add(cb.lessThanOrEqualTo(purchaseRoot.<Date>get(filter.getValueName()), date));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            break;
                        case HAS:
                            try {
                                Join<Product, Category> categoryJoin = productJoin.join("categories");
                                predicates.add(cb.equal(categoryJoin.get("id"), Integer.parseInt(filter.getValue())));
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                });
            });
        }

        cq.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Purchase> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}