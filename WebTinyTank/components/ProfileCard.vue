<script setup lang="ts">
import type { Profile } from '~/types/master'

const props = defineProps<{ profile: Profile }>()

const accuracy = computed(() => {
  const s = props.profile.stats
  return s.shotsFired ? (s.shotsHit * 100 / s.shotsFired).toFixed(2) : '0.00'
})
</script>

<template>
  <div>
    <h2 class="page-header text-center">{{ profile.username }}</h2>
    <p>Registered {{ timeAgo(profile.createdAt) }} from {{ profile.from }}</p>
    <p>Last seen {{ timeAgo(profile.updatedAt) }}</p>
    <table class="table">
      <tbody>
        <tr><th>Kills</th><td>{{ profile.stats.kills }} ({{ profile.stats.killsPG }}/g)</td></tr>
        <tr><th>Deaths</th><td>{{ profile.stats.deaths }} ({{ profile.stats.deathsPG }}/g)</td></tr>
        <tr><th>Score</th><td>{{ profile.stats.score }} ({{ profile.stats.scorePG }}/g)</td></tr>
        <tr><th>Accuracy</th><td>{{ accuracy }} %</td></tr>
      </tbody>
    </table>
  </div>
</template>
