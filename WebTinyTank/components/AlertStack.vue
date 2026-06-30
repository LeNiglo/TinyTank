<script setup lang="ts">
const { alerts, dismiss } = useAlerts()
</script>

<template>
  <div id="errors" class="container">
    <div
      v-for="a in alerts"
      :key="a.id"
      :class="`alert alert-${a.level} alert-dismissible`"
      role="alert"
    >
      <button type="button" class="close float-right" aria-label="Close" @click="dismiss(a.id)">
        <span aria-hidden="true">&times;</span>
      </button>
      <strong>{{ a.title }}</strong> <span v-html="a.message" />
    </div>
  </div>
</template>
