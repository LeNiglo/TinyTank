<script setup lang="ts">
import type { Tank } from '~/types/master'

useHead({ title: 'Tanks — TinyTank' })

const route = useRoute()
const api = useMasterApi()
const tanks = ref<Tank[]>([])

onMounted(async () => {
  try {
    tanks.value = await api.getTanks()
  } catch {
    // best-effort
  }
})

const selected = computed(() =>
  route.params.id
    ? tanks.value.find((t) => t._id === route.params.id || (t as any).name === route.params.id)
    : null
)
</script>

<template>
  <div>
    <h1>Tanks</h1>
    <TankCard v-if="selected" :tank="selected" />
    <div v-else class="row">
      <div v-for="t in tanks" :key="t._id" class="col-md-4">
        <NuxtLink :to="`/tank/${t._id}`">{{ (t as any).name ?? t._id }}</NuxtLink>
      </div>
    </div>
  </div>
</template>
