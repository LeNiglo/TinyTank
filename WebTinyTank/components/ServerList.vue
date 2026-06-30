<script setup lang="ts">
import type { Server } from '~/types/master'

defineProps<{ servers: Server[] }>()
</script>

<template>
  <table class="table">
    <thead>
      <tr>
        <th>Server Name</th>
        <th>Current Map</th>
        <th>Uptime</th>
        <th>Nb Players</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="s in servers" :key="s._id">
        <th>{{ s.name }}</th>
        <td>{{ s.map }}</td>
        <td>{{ timeAgo(s.started_at) }}</td>
        <td>{{ s.users.length }}</td>
      </tr>
    </tbody>
  </table>
</template>
