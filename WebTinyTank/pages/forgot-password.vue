<script setup lang="ts">
useHead({ title: 'Forgot password — TinyTank' })
</script>

<template>
  <div class="row text-center">
    <h3 class="page-header">Password lost ?</h3>
    <p class="lead">Password recovery is coming soon.</p>
    <p>In the meantime, please contact an admin to reset your password.</p>
    <div class="row text-center">
      <div class="col-md-6"><NuxtLink class="btn btn-primary" to="/" style="width: 100%">Home</NuxtLink></div>
      <div class="col-md-6"><NuxtLink class="btn btn-info" to="/login" style="width: 100%">Back to Login</NuxtLink></div>
    </div>
  </div>
</template>
