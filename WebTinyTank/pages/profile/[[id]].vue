<script setup lang="ts">
import type { Profile } from '~/types/master'

const route = useRoute()
const api = useMasterApi()
const auth = useAuth()
const { notify } = useAlerts()

const profile = ref<Profile | null>(null)
const loading = ref(true)
const notFound = ref(false)

async function load() {
  let id = (route.params.id as string) || ''
  if (!id) {
    auth.hydrate()
    id = auth.authId.value || ''
  }
  if (!id) {
    await navigateTo('/login')
    return
  }
  loading.value = true
  notFound.value = false
  try {
    profile.value = await api.getProfile(id)
    useHead({ title: `${profile.value.username}'s profile on TinyTank` })
  } catch {
    profile.value = null
    notFound.value = true
    notify('Maybe you mispelled it.', 'User not found', 'danger')
  } finally {
    loading.value = false
  }
}

onMounted(load)
watch(() => route.params.id, load)
</script>

<template>
  <div class="container">
    <h2 v-if="loading" class="page-header text-center">Loading ...</h2>
    <ProfileCard v-else-if="profile" :profile="profile" />
    <template v-else>
      <h2 class="page-header text-center">User Not Found.</h2>
      <SearchBox />
    </template>
  </div>
</template>
