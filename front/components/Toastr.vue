<template>
    <BToast class="wise-toastr" v-model="toastr.show" :variant="toastr.variant">
        <template #title>
            {{ toastr.title }}
        </template>
        {{ toastr.body }}
    </BToast>
</template>

<script setup lang="ts">

import { useUtilsStore } from '~/stores/utils';

const { getToastr } = storeToRefs(useUtilsStore());

const toastr  = getToastr;

</script>

<style lang="scss" >
.wise-toastr {
    position: fixed;
    z-index: 999;
    right: 0;

    .toast-header {
        display: flex !important;
        justify-content: space-between !important;
    }
}
</style>