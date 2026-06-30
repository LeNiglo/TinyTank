<script setup lang="ts">
definePageMeta({ layout: 'full' })
useHead({ title: 'TinyTank' })
</script>

<template>
  <div>
    <h1>Tiny Tank Official</h1>
    <HomeBanner />
    <div id="home">
      <section>
        <img src="/img/TinyBanner.png" class="img-responsive" style="margin: auto">
        <div class="container text-center">
          <h2 class="page-header">Fun Comes First</h2>
          <p>Our cute tiny tanks will make you enjoy every moment of gaming. Play in a lot of mods :</p>
          <ul class="list-unstyled">
            <li>Free For All <strong>[FFA]</strong></li>
            <li>Team DeathMatch <strong>[TDM]</strong></li>
            <li>King of the Hill <strong>[KTH]</strong></li>
            <li>Drop The Bomb <strong>[DTB]</strong></li>
          </ul>
        </div>
      </section>
      <section>
        <div class="container text-center">
          <h2 class="page-header">Rankable gaming</h2>
          <p>A system of ladder is included so playing with your friends can become a competitive match.</p>
          <p>
            You can also gain medals by playing some mod and reaching some score for example. Those medals will
            be visible by anyone who is looking at your profile.
          </p>
        </div>
      </section>
      <section>
        <div class="container text-center">
          <h2 class="page-header">Growing Community</h2>
          <div class="col-md-6">
            <p>Join our growing army !</p>
            <GlobalInfos />
          </div>
          <div class="col-md-6">
            <PaypalButton />
          </div>
        </div>
      </section>
    </div>
  </div>
</template>
