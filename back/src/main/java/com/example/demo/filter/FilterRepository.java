package com.example.demo.filter;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilterRepository extends JpaRepository<Filter, Long> {
    @Transactional
    void deleteByFilterGroup(FilterGroup filterGroup);

    List<Filter> findByFilterGroup(FilterGroup filterGroup);
}