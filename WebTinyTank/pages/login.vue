<script setup lang="ts">
useHead({ title: 'Login — TinyTank' })

const api = useMasterApi()
const auth = useAuth()
const { notify } = useAlerts()

const email = ref('')
const password = ref('')

async function submit() {
  if (!email.value || !password.value) return
  try {
    const r = await api.login({ login: email.value, password: password.value })
    auth.setSession(r)
    await navigateTo('/profile')
    notify(`Happy to see you, ${r.username} !`, 'Welcome Back !', 'success')
  } catch (e: any) {
    notify(e?.data?.message || e?.message || 'Login failed', 'Login failed, ', 'danger')
  }
}
</script>

<template>
  <div class="col-md-6 col-md-offset-3 text-center">
    <h2 class="page-header">Login</h2>
    <form id="login" role="login" @submit.prevent="submit">
      <div class="form-group">
        <input v-model="email" type="text" name="email" class="form-control" placeholder="email or username" required>
      </div>
      <div class="form-group">
        <input v-model="password" type="password" name="password" class="form-control" placeholder="password" required>
        <small class="text-right"><NuxtLink to="/forgot-password">Forgot password ?</NuxtLink></small>
      </div>
      <div class="form-group row text-center">
        <div class="col-md-6">
          <input type="submit" class="btn btn-success form-control" value="Login to your Account">
        </div>
        <div class="col-md-6">
          <NuxtLink to="/register" class="btn btn-warning form-control">Register</NuxtLink>
        </div>
      </div>
    </form>
  </div>
</template>
