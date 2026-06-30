<script setup lang="ts">
const query = ref('')
function submit() {
  if (query.value) navigateTo(`/profile/${encodeURIComponent(query.value)}`)
}
</script>

<template>
  <form class="text-center" role="search" @submit.prevent="submit">
    <div class="form-group">
      <div class="input-group">
        <span class="input-group-addon"><i class="glyphicon glyphicon-search" /></span>
        <input
          v-model="query"
          type="text"
          class="form-control"
          name="search"
          placeholder="Search"
        >
      </div>
    </div>
    <button type="submit" class="btn btn-primary">Search</button>
  </form>
</template>
