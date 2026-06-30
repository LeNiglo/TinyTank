<script setup lang="ts">
useHead({ title: 'Register — TinyTank' })

const api = useMasterApi()
const { notify } = useAlerts()

const countries = ['France', 'United Kingdom', 'Spain', 'Deutschland', 'Belgium', 'United States', 'Other']
const email = ref('')
const username = ref('')
const password = ref('')
const passwordV = ref('')
const from = ref('France')

const emailRe = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
const passwordRe = /^[\W\w]{7,99}$/
const usernameRe = /^[\w\s]{3,20}$/

const matchOk = computed(() => password.value.length > 0 && password.value === passwordV.value)

async function submit() {
  if (password.value !== passwordV.value) {
    notify('Passwords must match.', 'Attention,', 'warning')
    return
  }
  if (!(emailRe.test(email.value) && passwordRe.test(password.value) && usernameRe.test(username.value))) {
    notify("Parameters aren't correct, please try again with others.", 'Not enought strength.', 'danger')
    return
  }
  try {
    await api.register({ username: username.value, email: email.value, password: password.value, from: from.value })
    await navigateTo('/login')
    notify('An activation email has been sent to you. (required)', 'Congratulations !', 'success')
  } catch (e: any) {
    notify(e?.data?.message || e?.message || 'Registration failed', '', 'danger')
  }
}
</script>

<template>
  <div class="col-md-6 col-md-offset-3 text-center">
    <h2 class="page-header">Register</h2>
    <form id="register" role="register" @submit.prevent="submit">
      <div class="form-group">
        <input v-model="email" type="email" name="email" class="form-control" placeholder="email" required>
      </div>
      <div class="form-group">
        <input v-model="username" type="text" name="username" class="form-control" placeholder="username" required>
      </div>
      <div class="form-group" :class="{ 'has-success': matchOk }">
        <input v-model="password" type="password" name="password" class="form-control" placeholder="password" required>
        <input v-model="passwordV" type="password" name="password-v" class="form-control" placeholder="confirm" required>
      </div>
      <div class="form-group">
        <select v-model="from" name="from" class="form-control">
          <option v-for="c in countries" :key="c" :value="c">{{ c }}</option>
        </select>
      </div>
      <div class="form-group text-center">
        <input type="submit" class="btn btn-success form-control" value="Register">
      </div>
      <NuxtLink to="/login">Already an account ?</NuxtLink>
    </form>
  </div>
</template>
