<template>
    <div class="filters d-flex">
        <div v-for="filter in filters">
            <button  class="btn btn-warning" @click="$emit('select', filter)">{{ filter.name }} </button>
        </div>
    </div>
</template>

<script setup lang="ts">
import { useFiltersStore } from '~/stores/filters';

const { getFilters } = storeToRefs(useFiltersStore());

const filters = getFilters;
</script>

<style scoped>
.filters {
    color: #fff;
    border: none;
    border-radius: 50%;
    width: 3rem;
    height: 3rem;
    font-size: 1.5rem;
    position: fixed;
    bottom: 1rem;
    left: 1.5rem;
    transition: background-color 0.3s;
}

.btn {
    color: white !important;
    margin-left: 1rem;
    padding: 0.5rem 1rem !important;
    height: fit-content;
    width: max-content !important;
}
</style>