// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
    devtools: { enabled: true },
    modules: [
        '@bootstrap-vue-next/nuxt',
        '@pinia/nuxt',
        'nuxt-icon',
        'nuxt-lodash',
    ],
    css: ['bootstrap/dist/css/bootstrap.min.css'],
    build: {
        transpile: ['@vuepic/vue-datepicker']
    },
});
