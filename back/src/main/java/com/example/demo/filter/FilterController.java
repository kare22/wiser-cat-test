package com.example.demo.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public abstract class FilterController<T> {
    private FilterRepository filterRepository;
    private FilterGroupRepository filterGroupRepository;

    @Autowired
    public void setFilterRepository(FilterRepository filterRepository) {
        this.filterRepository = filterRepository;
    }

    @Autowired
    public void setFilterGroupRepository(FilterGroupRepository filterGroupRepository) {
        this.filterGroupRepository = filterGroupRepository;
    }

    protected abstract String getClassName();

    @GetMapping("/filter-groups")
    public ResponseEntity<?> getFilterGroups() {
        return ResponseEntity.ok(filterGroupRepository.findAll());
    }

    @PostMapping("/filter-groups")
    public ResponseEntity<FilterGroup> addFilterGroup(@RequestBody FilterGroup filterGroup) {
        filterGroup.setClassName(getClassName());

        filterGroupRepository.save(filterGroup);

        return ResponseEntity.ok(filterGroup);
    }

    @PutMapping("/filter-groups/{filterGroupId}")
    public ResponseEntity<?> updateFilterGroup(@PathVariable Long filterGroupId, @RequestBody FilterGroup updatedFilterGroup) {
        FilterGroup filterGroup = filterGroupRepository.findById(filterGroupId).orElse(null);
        if (filterGroup == null) {
            return ResponseEntity.notFound().build();
        }

        filterGroup.update(updatedFilterGroup);

        filterGroupRepository.save(filterGroup);

        return ResponseEntity.ok(filterGroup);
    }

    @DeleteMapping("/filter-groups/{filterGroupId}")
    public ResponseEntity<?> deleteFilterGroup(@PathVariable Long filterGroupId) {
        FilterGroup filterGroup = filterGroupRepository.findById(filterGroupId).orElse(null);
        if (filterGroup == null) {
            return ResponseEntity.notFound().build();
        }

        filterRepository.deleteByFilterGroup(filterGroup);

        filterGroupRepository.delete(filterGroup);

        return ResponseEntity.ok("Filter group and associated filters deleted successfully");
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<String> setActive(@PathVariable Long id, @RequestParam boolean isActive) {
        try {
            FilterGroup filterGroup = filterGroupRepository.findById(id)
                    .orElseThrow(() -> new Exception("FilterGroup not found with id: " + id));

            filterGroup.setActive(isActive);
            filterGroupRepository.save(filterGroup);


            return ResponseEntity.ok("FilterGroup status updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/filter-groups/{filterGroupId}/filters")
    public ResponseEntity<?> getFilters(@PathVariable Long filterGroupId) {
        FilterGroup filterGroup = filterGroupRepository.findById(filterGroupId).orElse(null);
        if (filterGroup == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(filterRepository.findByFilterGroup(filterGroup));
    }

    @PostMapping("/filter-groups/{filterGroupId}/filters")
    public ResponseEntity<Filter> addFilter(@PathVariable Long filterGroupId, @RequestBody Filter filter) {
        FilterGroup filterGroup = filterGroupRepository.findById(filterGroupId).orElse(null);
        if (filterGroup == null) {
            return ResponseEntity.notFound().build();
        }

        filter.setFilterGroup(filterGroup);

        filterRepository.save(filter);

        return ResponseEntity.ok(filter);
    }

    @PutMapping("/filter-groups/{filterGroupId}/filters/{filterId}")
    public ResponseEntity<?> updateFilter(@PathVariable Long filterGroupId, @PathVariable Long filterId, @RequestBody Filter filter) {
        FilterGroup filterGroup = filterGroupRepository.findById(filterGroupId).orElse(null);
        if (filterGroup == null) {
            return ResponseEntity.notFound().build();
        }

        Filter currentFilter = filterRepository.findById(filterId).orElse(null);
        if (currentFilter == null) {
            return ResponseEntity.notFound().build();
        }

        if (!currentFilter.getFilterGroup().equals(filterGroup)) {
            return ResponseEntity.badRequest().body("Filter does not belong to the specified filter group");
        }

        currentFilter.setValueName(filter.getValueName());
        currentFilter.setOperator(filter.getOperator());
        currentFilter.setValue(filter.getValue());

        filterRepository.save(currentFilter);

        return ResponseEntity.ok(currentFilter);
    }

    @DeleteMapping("/filter-groups/{filterGroupId}/filters/{filterId}")
    public ResponseEntity<?> deleteFilter(@PathVariable Long filterGroupId, @PathVariable Long filterId) {
        FilterGroup filterGroup = filterGroupRepository.findById(filterGroupId).orElse(null);
        if (filterGroup == null) {
            return ResponseEntity.notFound().build();
        }

        Filter filter = filterRepository.findById(filterId).orElse(null);
        if (filter == null) {
            return ResponseEntity.notFound().build();
        }

        if (!filter.getFilterGroup().equals(filterGroup)) {
            return ResponseEntity.badRequest().body("Filter does not belong to the specified filter group");
        }

        filterRepository.delete(filter);
        return ResponseEntity.ok("Filter deleted successfully");
    }
}