<script setup lang="ts">
const route = useRoute()
const api = useMasterApi()
const { notify } = useAlerts()

onMounted(async () => {
  try {
    await api.active(route.params.id as string)
    notify('Your account has been activated.', 'Well Done,', 'success')
  } catch {
    notify('Please try again.', 'Error, ', 'danger')
  } finally {
    await navigateTo('/profile')
  }
})
</script>

<template>
  <div class="container text-center">
    <h2 class="page-header">Activating…</h2>
  </div>
</template>
