<template>
    <div>
        <ResizableModal id="filter-modal" ref="filterModal" @on-size-change="adjustCriteriaAreaSize">
            <div class="mb-4 text-end">
                <BButton variant="primary" @click="() => toggleFilterModalPartOfPage()">
                    <Icon v-if="getFilterModalPartOfPage" name="⬇️"/>
                    <Icon v-else name="⬆️"/>
                </BButton>
            </div>
            <div class="d-block text-center">
                <div>
                    <div class="text-start">
                        Filter name:
                    </div>
                    <BFormInput v-model="filterGroupName" class="filter-group-name"/>
                </div>
                <div class="text-start mt-4">
                    Criteria:
                </div>
                <div ref="criteriaArea" class="filters-criteria-area" :style="{ height: criteriaAreaHeight + 'px' }">
                    <div v-for="filter in filters" class="d-flex mb-3">
                        <BFormSelect v-model="filter.valueName" :options="FIELDS" class="filter-modal-input" @change="filter.operator = null"></BFormSelect>
                        <BFormSelect v-model="filter.operator" :options="getOperators(filter)" class="filter-modal-input"></BFormSelect>
                        <BFormInput v-if="filter.valueName == 'name'" v-model="filter.value" class="filter-modal-input"/>
                        <BFormInput v-else-if="['amount', 'price', 'total'].includes(filter.valueName)" v-model="filter.value" type="number" class="filter-modal-input"/>
                        <VueDatePicker
                            v-else
                            class="filter-modal-input"
                            auto-apply
                            :enable-time-picker="false"
                            v-model="filter.value"
                        ></VueDatePicker>
                        <BButton @click="() => removeFilter(filter)" variant="danger">-</BButton>
                    </div>
                </div>
                <div class="text-center">
                    <BButton @click="add">+ Add</BButton>
                </div>
                <div class="mt-4">
                    Selection:
                    <BFormCheckboxGroup
                        v-model="selectedCategories"
                        :options="categories"
                        class="mb-3"
                        value-field="id"
                        text-field="name"
                        disabled-field="notEnabled"
                    />
                </div>
                <div class="filter-modal-error-area">
                    {{ errorText }}
                </div>
                <div class="text-center mt-3">
                    <BButton v-if="filterGroupId != null" @click="destroyFilterGroup" class="me-3" variant="danger">Delete</BButton>
                    <BButton @click="hideModal" class="me-3" variant="warning">Cancel</BButton>
                    <BButton @click="submitFilterGroup" variant="primary">Submit</BButton>
                </div>
            </div>
        </ResizableModal>
    </div>
</template>

<script setup lang="ts">
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'
import type { Filter } from '~/stores/filters';
import { FilterGroup, useFiltersStore } from '~/stores/filters';
import { cloneDeep } from 'lodash-es';
import { useUtilsStore } from '~/stores/utils';
import { ProductCategory, useProductsStore } from '~/stores/products';
import { usePurchasesStore } from '~/stores/purchases';
import { storeToRefs } from 'pinia';
const { showToastr, toggleFilterModalPartOfPage } = useUtilsStore();
const { getProductCategories } = useProductsStore();
const { storeFilterGroup, updateFilterGroup, deleteFilterGroup } = useFiltersStore();
const { fetchPurchases } = usePurchasesStore();
const { getFilterModalPartOfPage } = storeToRefs(useUtilsStore());

const FIELDS = {
    name: 'Product name',
    amount: 'Amount',
    price: 'Price',
    total: 'Total',
    createdAt: 'Created at',
}

const getOperators = (filter: Filter) => {
    switch (filter.valueName) {
        case 'name':
            return {
                'LESSER_THAN': 'Lesser than',
                'BEGINS_WITH': 'Begins with',
                'CONTAINS': 'Contains',
            };
        case 'amount':
        case 'price':
        case 'total':
            return {
                'EQUALS': 'Equals',
                'GREATER_THAN': 'Greater than',
                'LESSER_THAN': 'Lesser than',
            };
        case 'createdAt':
            return {
                'DATE_FROM': 'Date from',
                'DATE_TO': 'Date to',
            };
    }
};

const categories: ProductCategory[] = getProductCategories;

const selectedCategories: Ref<number[]> = ref([]);

const filters: Ref<Filter[]> = ref([]);

const criteriaArea: Ref = ref(null);
const add = () => {
    filters.value.push({
        valueName: 'amount',
        operator: 'EQUALS',
    });

    setTimeout(() => {
        if (criteriaArea.value) {
            criteriaArea.value.scrollTop = criteriaArea.value.scrollHeight;
        }
    }, 10);
};

const removeFilter = (filter: Filter) => {
    const index = filters.value.indexOf(filter);
    if (index !== -1) {
        filters.value.splice(index, 1);
    }
};

const errorText = ref('');
const validateForm = () => {
    if (!filterGroupName.value) {
        errorText.value = 'Filter must have a name';
    } else if (!filters.value.length && !selectedCategories.value.length) {
        errorText.value = 'No criteria or selection selected!';
    } else if (filters.value.filter((filter: Filter) => !filter.operator || !filter.valueName || !filter.value).length > 0) {
        errorText.value = 'All filter fields must be filled.';
    } else {
        return true;
    }

    return false;
};

const filterGroupName = ref('');
let filterGroupId: number|null;
const submitFilterGroup = async() => {
    if (!validateForm()) {
        return;
    }

    const selectedCategoryToFilter = (id: number) => ({
        valueName: 'category',
        operator: 'HAS',
        value: id.toString(),
    });
    const payload = new FilterGroup({
        id: filterGroupId,
        name: filterGroupName,
        filters: [...filters.value, ...selectedCategories.value.map(selectedCategoryToFilter)],
    });

    if (filterGroupId) {
        await updateFilterGroup(payload);
        showToastr({
            body: 'Filter updated',
        });
    } else {
        await storeFilterGroup(payload)

        showToastr({
            body: 'Filter added',
        });
    }

    await fetchPurchases();

    hideModal();
};

const destroyFilterGroup = async() => {
    const result = await deleteFilterGroup(filterGroupId);

    if (result) {
        await fetchPurchases();

        showToastr({
            body: 'Filter removed',
        });

        hideModal();
    } else {
        showToastr({
            body: 'Error removing filter',
            variant: 'Warning',
        });
    }
};

const criteriaAreaHeight = ref(160);
const adjustCriteriaAreaSize = (size: number) => {
    console.log(size);
    criteriaAreaHeight.value = Math.max(0, size - 555) + 160;
};

const filterModal = ref(null);
const show = async(filterGroup?: FilterGroup) => {
    if (filterGroup) {
        filterGroupId = filterGroup.id;
        filterGroupName.value = filterGroup.name;
        filters.value = cloneDeep(filterGroup.filters);
        selectedCategories.value = filters.value
            .filter((filter: Filter) => filter.operator === 'HAS')
            .map((filter: Filter) => filter?.value ? +filter.value : 0);
        filters.value = filters.value.filter((filter: Filter) => filter.operator !== 'HAS');
    } else {
        filterGroupId = null;
        filterGroupName.value = '';
        filters.value = [
            {
                valueName: 'amount',
                operator: 'EQUALS',
            },
        ];
        selectedCategories.value = [];
    }

    // @ts-ignore: Object is possibly 'null'.
    filterModal.value.show();
};
const hideModal = () => {
    // @ts-ignore: Object is possibly 'null'.
    filterModal.value.hide();
};

defineExpose({
    show,
});
</script>

<style lang="scss">
#filter-modal {
  *.modal-body {
    padding: 0rem 1rem;
  }
}

.filter-group-name {
    width: 30rem;
}

.filter-modal-input {
    width: 20rem !important;
    margin-right: 2rem !important;
}

.filters-criteria-area {
    height: 10rem;
    overflow: auto;
    margin-bottom: 2rem;
}
</style>