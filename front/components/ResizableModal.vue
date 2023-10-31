<template>
    <BModal :id="id" ref="resizeableModal" hide-footer hide-header size="xl" :fullscreen="getFilterModalPartOfPage" :class="getFilterModalPartOfPage && 'modal-part-of-page'">
        <div class="resizable" :style="{ height: modalHeight + 'px' }">
            <div class="resize-handle top" @mousedown="startResize('top', $event)"></div>
            <div class="content">
                <slot></slot>
            </div>
            <div class="resize-handle bottom" @mousedown="startResize('bottom', $event)"></div>
        </div>
    </BModal>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, defineProps, defineExpose } from 'vue';
import { storeToRefs } from 'pinia';
import { useUtilsStore } from '~/stores/utils';

const { getFilterModalPartOfPage } = storeToRefs(useUtilsStore());

const resizeableModal = ref(null);
const isResizing = ref(false);
const modalHeight = ref(555);
const minHeight = 555;
let maxHeight = 0;
let startY = 0;
let resizeDirection: null | string = null;

const startResize = (direction: any, event: any) => {
    isResizing.value = true;
    resizeDirection = direction;
    startY = event.clientY;
    window.addEventListener('mousemove', resize);
    window.addEventListener('mouseup', stopResize);
};

const resize = (event: any) => {
    if (isResizing.value) {
        const diffY = event.clientY - startY;
        if (resizeDirection === 'top') {
            modalHeight.value = Math.max(minHeight, modalHeight.value - diffY);
        } else if (resizeDirection === 'bottom') {
            modalHeight.value = Math.max(minHeight, Math.min(maxHeight, modalHeight.value + diffY));
        }
        startY = event.clientY;

        emit('onSizeChange', modalHeight.value);
    }
};

const stopResize = () => {
    isResizing.value = false;
    window.removeEventListener('mousemove', resize);
    window.removeEventListener('mouseup', stopResize);
};

const show = () => {
    // @ts-ignore: Object is possibly 'null'.
    resizeableModal.value.show();
};

const hide = () => {
    // @ts-ignore: Object is possibly 'null'.
    resizeableModal.value.hide();
};

onMounted(() => {
    maxHeight = window.innerHeight;
});

onBeforeUnmount(() => {
    window.removeEventListener('mousemove', resize);
    window.removeEventListener('mouseup', stopResize);
});

defineProps<{
    id: string;
}>();

defineExpose({
    show,
    hide,
});

const emit = defineEmits([
    'onSizeChange',
]);
</script>


<style lang="scss">
.modal-part-of-page {
    *.filter-modal-input {
        width: 30rem !important;
    }

    .filters-criteria-area {
        height: 40rem !important;
        overflow: auto;
    }

    * {
        .resizable {
            height: 100% !important;
        }

        .resize-handle {
            display: none !important;
        }

        .content {
            height: 100% !important;
            overflow: auto;
        }
    }
}
</style>
<style lang="scss" scoped>
.resizable {
    height: 200px;
    position: relative;
    overflow: hidden;
}

.resize-handle {
    width: 100%;
    height: 5px;
    background-color: #0074d9;
    cursor: row-resize;
    position: absolute;
    left: 0;
    user-select: none;
}

.resize-handle.top {
    top: 0;
}

.resize-handle.bottom {
    bottom: 0;
}

.content {
    height: calc(100% - 10px);
    overflow: auto;
}
</style>