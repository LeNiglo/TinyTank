<template>
  <footer class="container-fluid">
    <hr>
    <div class="container text-center">
      <div class="row">
        <div class="col-md-4">
          <h2 class="page-information">Website</h2>
          <ul class="list-unstyled">
            <li><NuxtLink to="/">Home</NuxtLink></li>
            <li><NuxtLink to="/login">Login / Register</NuxtLink></li>
            <li><NuxtLink to="/about">About</NuxtLink></li>
            <li><NuxtLink to="/download">Download</NuxtLink></li>
          </ul>
        </div>
        <div class="col-md-4">
          <h2 class="page-information">Contact</h2>
          <ul class="list-unstyled">
            <li><a href="mailto:lefrantguillaume@gmail.com">Lefrant Guillaume</a></li>
            <li><a href="mailto:kevin.draym@gmail.com">Kévin Andres</a></li>
            <li><a href="mailto:styve.simonneau@gmail.com">Styve Simonneau</a></li>
            <li><a href="mailto:alexandre.quintin@hotmail.fr">Alexandre Quintin</a></li>
          </ul>
        </div>
        <div class="col-md-4">
          <h2 class="page-information">Contributing</h2>
          <ul class="list-unstyled">
            <li><NuxtLink to="/about#donate">Donate</NuxtLink></li>
            <li><NuxtLink to="/about#content">Content</NuxtLink></li>
            <li><NuxtLink to="/about#development">Development</NuxtLink></li>
          </ul>
        </div>
      </div>
    </div>
  </footer>
</template>
