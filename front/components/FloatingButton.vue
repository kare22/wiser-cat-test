<template>
    <button class="floating-button"><Icon name="🔍" /></button>
</template>

<script setup lang="ts">

</script>

<style scoped>
.floating-button {
    background-color: #ffb700;
    color: #fff;
    border: none;
    border-radius: 50%;
    width: 3rem;
    height: 3rem;
    font-size: 1.5rem;
    position: fixed;
    bottom: 1rem;
    right: 1.5rem;
    cursor: pointer;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    transition: background-color 0.3s;
}

.floating-button:hover {
    background-color: #ffca2c;
}
</style>