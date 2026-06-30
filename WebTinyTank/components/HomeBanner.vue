<script setup lang="ts">
const messages = ['Have Fun', 'Get Blasted', 'Rank Up']
const colors = ['#7F0000', '#CC0000', '#7F2626']
const idx = ref(0)
let timer: ReturnType<typeof setInterval> | undefined

onMounted(() => {
  timer = setInterval(() => {
    idx.value = (idx.value + 1) % messages.length
  }, 3000)
})
onUnmounted(() => clearInterval(timer))
</script>

<template>
  <h2 class="text-primary text-center">
    <span id="tinyHomeMessage" :style="{ color: colors[idx] }">{{ messages[idx] }}</span>, on Tiny Tank.
  </h2>
</template>
