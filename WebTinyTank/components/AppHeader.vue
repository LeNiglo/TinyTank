<script setup lang="ts">
const auth = useAuth()
onMounted(() => auth.hydrate())
function onLogout() {
  auth.logout()
  navigateTo('/')
}
</script>

<template>
  <nav class="navbar navbar-default">
    <div class="container-fluid">
      <div class="navbar-header">
        <NuxtLink class="navbar-brand" to="/">TinyTank</NuxtLink>
      </div>
      <div class="navbar-collapse">
        <ul class="nav navbar-nav">
          <template v-if="auth.isConnected.value">
            <li><NuxtLink to="/profile">{{ auth.username.value }}</NuxtLink></li>
            <li>
              <a id="logout" title="Logout" style="cursor: pointer" @click="onLogout">
                <i class="glyphicon glyphicon-off" />
              </a>
            </li>
          </template>
          <li v-else><NuxtLink to="/login">Login/Register</NuxtLink></li>
          <li><NuxtLink to="/ladder">Ladder</NuxtLink></li>
          <li><NuxtLink to="/servers-list">Servers List</NuxtLink></li>
          <li><NuxtLink to="/about">About</NuxtLink></li>
        </ul>
        <div class="navbar-form navbar-right">
          <SearchBox />
        </div>
        <ul class="nav navbar-nav navbar-right">
          <li><NuxtLink to="/download">Download</NuxtLink></li>
          <li><a href="https://github.com/LeNiglo/TinyTank" target="_blank">GitHub</a></li>
        </ul>
      </div>
    </div>
  </nav>
</template>
