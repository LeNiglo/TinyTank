<template>
  <div>
    <img src="/img/banner.png" alt="banner" style="width: 100%" class="img-responsive">
    <AppHeader />
    <div class="container-fluid">
      <AlertStack />
      <slot />
    </div>
    <AppFooter />
  </div>
</template>
