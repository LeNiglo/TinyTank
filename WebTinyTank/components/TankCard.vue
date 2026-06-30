<script setup lang="ts">
import type { Tank } from '~/types/master'

defineProps<{ tank: Tank }>()
</script>

<template>
  <div class="tank-card">
    <h3>{{ tank.name ?? tank._id }}</h3>
    <table class="table">
      <tbody>
        <tr v-for="(value, key) in tank" :key="key">
          <th>{{ key }}</th>
          <td>{{ value }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
