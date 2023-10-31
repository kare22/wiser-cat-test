export interface ProductsState {
    products: Product[],
    categories: ProductCategory[],
}
export const useProductsStore = defineStore('products', {
    state: (): ProductsState => ({
        products: [],
        categories: [],
    }),
    getters: {
        getProducts: (state) => state.products.map(Product.toObject) as Product[],
        getProductCategories: (state) => state.categories.map(ProductCategory.toObject) as ProductCategory[],
    },
    actions: {
        async fetchProducts() {
            const { data: products } = await useFetch<Product[]>('http://localhost:8086/products')
            if (products) {
                this.products = products.value ?? [];
            }
        },
        async fetchProductCategories() {
            const { data: categories } = await useFetch<Product[]>('http://localhost:8086/products-categories')
            if (categories) {
                this.categories = categories.value ?? [];
            }
        },
    },
})


export class Product {
    id: number;
    name: string;
    price: number;
    categories: ProductCategory[];


    constructor(data: any) {
        this.id = data?.id;
        this.name = data?.name;
        this.price = data?.price;
        this.categories = data?.categories.map((category: any) => new ProductCategory(category));
    }

    public static toObject(data: any): Product {
        return new Product(data);
    }
}

export class ProductCategory {
    id: number;
    name: string;


    constructor(data: any) {
        this.id = data?.id;
        this.name = data?.name;
    }

    public static toObject(data: any): ProductCategory {
        return new ProductCategory(data);
    }
}