<script setup lang="ts">
definePageMeta({ layout: 'full' })
useHead({ title: 'Download — TinyTank' })
</script>

<template>
  <div>
    <h1 class="text-center page-header">Download TinyTank Now !</h1>
    <div class="container text-center">
      <div class="jumbotron text-left">
        <h1>TinyTank</h1>
        <p>Compete with other people and enjoy your moment !</p>
      </div>
      <div class="col-md-6">
        <a href="/downloads/TinyTank.zip" target="_blank">
          <img src="/img/download.png" alt="download" class="img-responsive">
        </a>
      </div>
      <div class="col-md-6">
        <NuxtLink to="/register">
          <img src="/img/signup.png" alt="register" class="img-responsive">
        </NuxtLink>
      </div>
    </div>
  </div>
</template>
