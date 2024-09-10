<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div class="container">
    <header>
      <div class="wrapper">
        <nav>
          <RouterLink to="/">Home</RouterLink>
          <RouterLink to="/vias">Vías</RouterLink>
          <RouterLink to="/tipo-vias">Tipo Vias</RouterLink>
        </nav>
      </div>
    </header>
    <RouterView class="router-view" />
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.router-view {
  flex: none;
  height: 600px;
  width: 100%;
  overflow: auto;
}

header {
  background-color: #f8f9fa;
  padding: 1rem;
}

.wrapper {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: center; /* Centra el contenido horizontalmente */
}

nav {
  display: flex;
  gap: 1rem;
}
</style>