<script setup lang="ts">
import type { Server } from '~/types/master'

useHead({ title: 'Servers List — TinyTank' })

const api = useMasterApi()
const servers = ref<Server[]>([])

async function refresh() {
  try {
    servers.value = await api.getServers()
  } catch {
    // best-effort
  }
}

usePolling(refresh, 60_000)
</script>

<template>
  <div>
    <h1>Servers List</h1>
    <ServerList :servers="servers" />
  </div>
</template>
