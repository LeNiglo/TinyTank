<script setup lang="ts">
import type { Infos } from '~/types/master'

const api = useMasterApi()
const infos = ref<Infos | null>(null)

async function refresh() {
  try {
    infos.value = await api.getInfos()
  } catch {
    // swallow — legacy behaviour: infos are best-effort
  }
}

usePolling(refresh, 120_000)
</script>

<template>
  <div>
    <p>{{ infos?.nb_users ?? 0 }} registered users.</p>
    <p v-if="infos?.last">
      Greetings to
      <NuxtLink :to="`/profile/${infos.last._id}`">{{ infos.last.username }}</NuxtLink>.
      Joined us {{ timeAgo(infos.last.createdAt) }}
    </p>
  </div>
</template>
