package com.example.demo.filter;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class FilterGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @OneToMany(mappedBy = "filterGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Filter> filters = new ArrayList<>();

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void update(FilterGroup updatedFilterGroup) {
        this.name = updatedFilterGroup.name;

        this.filters.clear();
        this.filters.addAll(updatedFilterGroup.filters);
    }

    public boolean isAssignableTo(Class<?> targetClass) {
        try {
            Class<?> filterClass = Class.forName(className);
            return targetClass.isAssignableFrom(filterClass);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
