package com.example.demo.filter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "filter")
public class Filter {
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "value_name", nullable = false)
    private String valueName;

    @Column(name = "operator", nullable = false)
    private FilterOperator operator;

    @Column(name = "value", nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "filter_group_id", nullable = false)
    @JsonBackReference
    private FilterGroup filterGroup;

    public Filter() {

    }

    public Filter(String valueName, FilterOperator operator, String value, FilterGroup filterGroup) {
        this.valueName = valueName;
        this.operator = operator;
        this.value = value;
        this.filterGroup = filterGroup;
    }

    public long getId() {
        return id;
    }

    public String getValueName() {
        return valueName;
    }

    public FilterOperator getOperator() {
        return operator;
    }

    public String getValue() {
        return value;
    }

    public FilterGroup getFilterGroup() {
        return filterGroup;
    }

    public void setFilterGroup(FilterGroup filterGroup) {
        this.filterGroup = filterGroup;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public void setOperator(FilterOperator operator) {
        this.operator = operator;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
