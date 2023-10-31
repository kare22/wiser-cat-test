export interface FiltersState {
    filterGroups: FilterGroup[],
}

export const useFiltersStore = defineStore('filters', {
    state: (): FiltersState => ({
        filterGroups: [],
    }),
    getters: {
        getFilters: (state) => state.filterGroups.map(FilterGroup.toObject) as FilterGroup[],
    },
    actions: {
        async fetchFilters() {
            const { data: filterGroups } = await useFetch<FilterGroup[]>('http://localhost:8086/purchases/filter-groups')
            if (filterGroups) {
                this.filterGroups = filterGroups.value ?? [];
            }
        },
        async storeFilterGroup(filterGroup: FilterGroup) {
            const body = filterGroup.toJson();
            const { data: newFilterGroup } = await useFetch<FilterGroup[]>('http://localhost:8086/purchases/filter-groups', { method: 'POST', body, })

            this.filterGroups.push(new FilterGroup(newFilterGroup.value));
        },
        async updateFilterGroup(filterGroup: FilterGroup) {
            const body = filterGroup.toJson();
            const { data: newFilterGroup } = await useFetch<FilterGroup[]>(`http://localhost:8086/purchases/filter-groups/${filterGroup.id}`, { method: 'PUT', body, })

            const index = this.filterGroups.findIndex((group: FilterGroup) => group.id === filterGroup.id);
            if (index !== -1) {
                this.filterGroups[index] = new FilterGroup(newFilterGroup.value);
            }
        },
        async deleteFilterGroup(filterGroupId: number|null) {
            if (filterGroupId == null) {
                return;
            }
            try {
                await useFetch<FilterGroup[]>(`http://localhost:8086/purchases/filter-groups/${filterGroupId}`, { method: 'DELETE'})
                this.filterGroups = this.filterGroups.filter((group: FilterGroup) => group.id !== filterGroupId);

                return true;
            } catch (error) {
                console.error(error);
                return false;
            }
        },
    },
})


export class FilterGroup {
    id: number;
    name: string;
    filters: Filter[];


    constructor(data: any) {
        this.id = data?.id;
        this.name = data?.name;
        this.filters = data?.filters ? data.filters.map(Filter.toObject) : undefined;
    }

    public static toObject(data: any): FilterGroup {
        return new FilterGroup(data);
    }

    toJson(): any {
        return {
            id: this.id,
            name: this.name,
            filters: this.filters.map(Filter.toJson),
        }
    }
}
export class Filter {
    id?: number;
    valueName?: string;
    operator?: string;
    value?: string;


    constructor(data: any) {
        this.id = data?.id;
        this.valueName = data?.valueName;
        this.operator = data?.operator;
        this.value = data?.value;
    }

    public static toObject(data: any): Filter {
        return new Filter(data);
    }

    public static toJson(data: Filter): any {
        return {
            id: data?.id,
            valueName: data?.valueName,
            operator: data?.operator,
            value: data?.value,
        }
    }
}