import { Product } from '~/stores/products';

export interface PurchasesState {
    purchases: Purchase[],
}
export const usePurchasesStore = defineStore('purchases', {
    state: (): PurchasesState => ({
        purchases: [],
    }),
    getters: {
        getPurchases: (state) => state.purchases.map(Purchase.toObject) as Purchase[],
    },
    actions: {
        async fetchPurchases() {
            const { data: purchases } = await useFetch<Purchase[]>('http://localhost:8086/purchases/filtered')
            if (purchases) {
                this.purchases = purchases.value ?? [];
            }
        },
    },
})


export class Purchase {
    id: number;
    product?: Product;
    amount: number;
    createdAt?: Date;
    cancelledAt?: Date;


    constructor(data: any) {
        this.id = data?.id;
        this.product = data?.product ? new Product(data.product) : undefined;
        this.amount = data?.amount;
        this.createdAt = data?.createdAt ? new Date(data.createdAt) : undefined;
        this.cancelledAt = data?.cancelledAt ? new Date(data.cancelledAt) : undefined;
    }

    public static toObject(data: any): Purchase {
        return new Purchase(data);
    }
}