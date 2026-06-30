<script setup lang="ts">
import type { LadderRow } from '~/types/master'

defineProps<{ rows: LadderRow[] }>()

const colors = ['#F15841', '#A83A2A', '#4D241E']
function rowStyle(i: number) {
  return i < 3 ? { fontSize: `${28 - 3 * i}px` } : {}
}
function rankStyle(i: number) {
  return i < 3 ? { color: colors[i] } : {}
}
</script>

<template>
  <table id="ladder" class="table table-hover" style="table-layout: fixed">
    <thead>
      <tr>
        <th>Rank</th>
        <th>Username</th>
        <th>Games Played</th>
        <th>Kill Count</th>
        <th>Accuracy</th>
        <th />
      </tr>
    </thead>
    <tbody>
      <tr v-for="(row, i) in rows" :key="row._id" :style="rowStyle(i)">
        <th class="rank" :style="rankStyle(i)">{{ row.rank }}</th>
        <td class="username"><NuxtLink :to="`/profile/${row._id}`">{{ row.username }}</NuxtLink></td>
        <td>{{ row.gamesPlayed }}</td>
        <td>{{ row.killCount }}</td>
        <td>{{ row.accuracy }} %</td>
        <td class="medal">
          <i v-if="i === 0" class="glyphicon glyphicon-star" style="color: gold" />
        </td>
      </tr>
    </tbody>
  </table>
</template>
