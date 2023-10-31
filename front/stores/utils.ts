import { variant } from 'postcss-minify-font-values/types/lib/keywords';

export interface UtilsState {
    toastr: Toastr,
    filterModalPartOfPage: boolean,
}

export interface Toastr {
    title?: string,
    body?: string,
    variant?: string,
    show?: boolean,
}

export const useUtilsStore = defineStore('utils', {
    state: (): UtilsState => ({
        toastr: {
            title: 'Notification',
            body: 'Text',
            variant: 'info',
            show: false,
        },
        filterModalPartOfPage: false,
    }),
    getters: {
        getToastr: (state) => state.toastr,
        getFilterModalPartOfPage: (state) => state.filterModalPartOfPage,
    },
    actions: {
        showToastr(toastr: Toastr) {
            this.toastr.show = true;
            this.toastr.title = toastr.title || 'Notification';
            this.toastr.body = toastr.body;
            this.toastr.variant = toastr.variant || 'info';

            setTimeout(() => {
                this.toastr.show = false;
            }, 2500);
        },
        toggleFilterModalPartOfPage() {
            console.log('filterModalPartOfPage', this.filterModalPartOfPage);
            this.filterModalPartOfPage = !this.filterModalPartOfPage;
        },
    }
})