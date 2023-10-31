<template>
    <div>
        <Toastr />
        <FilterModal ref="filterModal"/>
        <BTable
            id="purchases-table"
            :fields="fields"
            :items="purchases"
            striped
            hover
            stacked="md"
        >
            <template #cell(categories)="{ item }">
                <BBadge
                    v-for="category in item.product.categories"
                    variant="primary"
                    class="me-2"
                >
                    {{ category.name }}
                </BBadge>
            </template>
            <template #cell(createdAt)="{ item }">
                <span class="d-md-none">Created at: </span>{{ dateToString(item.createdAt) }}
            </template>
            <template #cell(price)="{ item }">
                <span class="d-md-none">Price: </span>{{ (item.product.price).toFixed(2) }}
            </template>
            <template #cell(total)="{ item }">
                <span class="d-md-none">Total: </span> {{ (item.product.price * item.amount).toFixed(2) }}
            </template>
            <template #cell(amount)="{ item }">
                <span class="d-md-none">Amount: </span> {{ item.amount }}
            </template>
        </BTable>

        <FloatingButton @click="() => openFilterModal()" />
        <ActiveFilters @select="(filterGroup) => {
            openFilterModal(filterGroup);
        }"/>
    </div>
</template>

<script setup lang="ts">
import { useProductsStore } from '~/stores/products';
import { usePurchasesStore } from '~/stores/purchases';
import { dateToString } from '~/helpers/date-helper';
import {  FilterGroup, useFiltersStore } from '~/stores/filters';

const { fetchProducts, fetchProductCategories } = useProductsStore();
const { fetchPurchases } = usePurchasesStore();
const { getPurchases } = storeToRefs(usePurchasesStore());
const { fetchFilters } = useFiltersStore();

await fetchProducts();
await fetchPurchases();
await fetchFilters();
await fetchProductCategories();

const purchases = getPurchases;

const fields = [
    {
        label: 'Product name',
        key: 'product.name',
    },
    {
        label: 'Product categories',
        key: 'categories',
    },
    {
        label: 'Date of purchase',
        key: 'createdAt',
    },
    {
        label: 'Product price',
        key: 'price',
    },
    {
        label: 'Amount',
        key: 'amount',
    },
    {
        label: 'Total',
        key: 'total',
    },
];


const filterModal = ref(null);
const openFilterModal = (filterGroup?: FilterGroup) => {
    // @ts-ignore: Object is possibly 'null'.
    filterModal.value.show(filterGroup);
};

</script>

<style lang="scss">
#purchases-table {
    margin-bottom: 5rem;
    * th {
        background: #ffb700 !important;
        color: white;
        position: sticky;
        top: 0; /* Don't forget this, required for the stickiness */
        box-shadow: 0 2px 2px -1px rgba(0, 0, 0, 0.4);
    }
}
</style>