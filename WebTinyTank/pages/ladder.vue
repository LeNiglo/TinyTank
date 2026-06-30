<script setup lang="ts">
import type { LadderRow } from '~/types/master'

useHead({ title: 'Ladder — TinyTank' })

const api = useMasterApi()
const rows = ref<LadderRow[]>([])

async function refresh() {
  try {
    rows.value = await api.getLadder()
  } catch {
    // best-effort
  }
}

usePolling(refresh, 60_000)
</script>

<template>
  <div>
    <h1>Ladder</h1>
    <LadderTable :rows="rows" />
  </div>
</template>
